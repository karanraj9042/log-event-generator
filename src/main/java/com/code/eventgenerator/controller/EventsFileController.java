package com.code.eventgenerator.controller;

import com.code.eventgenerator.comparator.EventSortByDuration;
import com.code.eventgenerator.model.Event;
import com.code.eventgenerator.service.EventFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EventsFileController {
    private static final Logger logger = LoggerFactory.getLogger(EventsFileController.class);

    @Autowired
    private EventFileService eventFileService;

    @GetMapping
    public @ResponseBody
    CompletableFuture<ResponseEntity> getAllFilteredEvents() throws SQLException, IOException {
        logger.info("List of all events log with their duration.");
        return eventFileService.saveEvents().<ResponseEntity>thenApply(ResponseEntity::ok)
                .exceptionally(handleGetCarFailure);
    }

    @GetMapping("/largerevents")
    public @ResponseBody
    ResponseEntity getLargestEventByDuration() throws SQLException, IOException {
        List<Event> eventArrayList = eventFileService.getEventJsonFromFile();
        logger.info("List of all events log with their duration,{}",eventArrayList);
        Collections.sort(eventArrayList,new EventSortByDuration());
        logger.info("List of all sorted events log with their duration,{}",eventArrayList);
        List<Event> largestEventArrayList = eventArrayList.stream().filter(event ->(event.getEventDuration()== eventArrayList.get(eventArrayList.size()-1).getEventDuration())).collect(Collectors.toList());
        logger.info("List of all larger events log {}",largestEventArrayList);
        return ResponseEntity.ok().body("Largest event is: -> "+ largestEventArrayList);
    }


    private static Function<Throwable, ResponseEntity<? extends List<Event>>> handleGetCarFailure = throwable -> {
        logger.error("Failed to read records: {}", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };



}
