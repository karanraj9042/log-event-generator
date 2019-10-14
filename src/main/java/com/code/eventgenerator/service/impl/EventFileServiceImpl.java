package com.code.eventgenerator.service.impl;

import com.code.eventgenerator.model.Event;
import com.code.eventgenerator.model.EventJson;
import com.code.eventgenerator.repository.EventsFileRepository;
import com.code.eventgenerator.service.EventFileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Component
public class EventFileServiceImpl implements EventFileService {
    private static final Logger logger = LoggerFactory.getLogger(EventFileServiceImpl.class);

    @Autowired
    private EventsFileRepository eventsFileRepository;

    @Override
    public List getEventJsonFromFile() throws IOException {
        final String eventsFilePath = System.getProperty("eventFilePath");
        if(logger.isInfoEnabled()){
            logger.info(String.format("json file path: %s ",eventsFilePath));
        }
        File file = new File(eventsFilePath);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,EventJson> eventJsonMap = new ConcurrentHashMap<>();
        List<Event> eventList = new ArrayList<>();

        try (Stream linesStream = Files.lines(file.toPath())) {
            linesStream.forEach(line -> {
                try {
                 EventJson convertedObject = objectMapper.readValue(line.toString(), EventJson.class);
                 if(eventJsonMap.containsKey(convertedObject.getId())){
                     eventList.add(getList(eventJsonMap,convertedObject));
                     eventJsonMap.remove(convertedObject.getId());
                 }else{
                     eventJsonMap.put(convertedObject.getId(),convertedObject);
                 }

                } catch (IOException e) {
                    logger.error(String.format("Json conversion issue occured  {%s} ",e.getLocalizedMessage()));
                }
            });
        } catch (IOException e) {
            logger.error(String.format("Internal issue occured while reading files  {%s} ",e.getLocalizedMessage()));
            throw e;
        }
        logger.info("Event Log list with unique event id and duration {}",eventList);
       return eventList;
    }

    @Async
    public CompletableFuture<Iterable<Event>> saveEvents() throws SQLException, IOException {
        final long start = System.currentTimeMillis();
        List<Event> events = getEventJsonFromFile();
        if(logger.isInfoEnabled()){
            logger.info(String.format("Saving a list of Event Logs of size {%d} records", events.size()));
        }
        Iterable<Event> eventIterable = eventsFileRepository.saveAll(events);
        if(logger.isInfoEnabled()){
            logger.info(String.format("Elapsed time: {%d%n}", (System.currentTimeMillis() - start)));
        }
        return CompletableFuture.completedFuture(eventIterable);
    }

    public Event getList(Map<String,EventJson> eventJsonMap,EventJson newEventJson){
        EventJson previousEventLog = eventJsonMap.get(newEventJson.getId());
       return checkEventDuration(previousEventLog,newEventJson);
    }

    private Event checkEventDuration(EventJson previousEventLog,EventJson newEventJson){
        long timeDiff;
        if(Long.parseLong(previousEventLog.getTimestamp()) < Long.parseLong(newEventJson.getTimestamp())){
            timeDiff = Math.subtractExact(Long.parseLong(newEventJson.getTimestamp()),Long.parseLong(previousEventLog.getTimestamp()));
        }
        else{
            timeDiff = Math.subtractExact(Long.parseLong(previousEventLog.getTimestamp()),Long.parseLong(newEventJson.getTimestamp()));
        }
       return convertIntoEventPoJO(newEventJson,timeDiff);
    }

    private Event convertIntoEventPoJO(EventJson eventJson,long timeDiff){
        Event event = new Event();
        event.setEventId(eventJson.getId());
        event.setEventDuration(timeDiff);
        if(!StringUtils.isEmpty(eventJson.getHost())){
            event.setHost(eventJson.getHost());
        }
        if(!StringUtils.isEmpty(eventJson.getState())){
            event.setType(eventJson.getType());
        }
        if(timeDiff > 4L){
            event.setAlert(true);
        }
        return event;
    }
}
