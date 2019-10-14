package com.code.eventgenerator.service;

import com.code.eventgenerator.model.Event;
import com.code.eventgenerator.repository.EventsFileRepository;
import com.code.eventgenerator.service.impl.EventFileServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RunWith(SpringJUnit4ClassRunner.class)
public class EventsFileServiceImplTest {

    private final String filePath = "F:\\logback.txt";

    private final String falseFilePath = "F:\\test.txt";

    @InjectMocks
    private EventFileServiceImpl eventFileService;

    @Mock
    private EventsFileRepository eventsFileRepository;



    @Test
    public void testGetEventJsonFromFile(){
        System.setProperty("eventFilePath",filePath);
        List eventList = null;
        try {
            eventList = eventFileService.getEventJsonFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(eventList);
        Assert.assertEquals(eventList.size(),9);
    }

    @Test
    public void testGetEventJsonFromNullPath(){
        System.setProperty("eventFilePath",falseFilePath);
        List eventList = null;
        try {
            eventList = eventFileService.getEventJsonFromFile();
        } catch (IOException e) {
            Assert.assertNotNull(e.getMessage());
        }

    }

    @Test
    public void testGetEventsFromNullPath(){
        System.setProperty("eventFilePath",falseFilePath);
        List eventList = null;
        try {
            eventList = eventFileService.getEventJsonFromFile();
        } catch (IOException e) {
        }
        Assert.assertNull(eventList);
    }

    @Test
    public void testSaveEvents() {
        System.setProperty("eventFilePath",filePath);
        try {
          Mockito.when(eventsFileRepository.saveAll(Mockito.anyList())).thenReturn(new ArrayList<>());
            CompletableFuture<Iterable<Event>> future = eventFileService.saveEvents();
            Assert.assertNotNull(future);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }

}
