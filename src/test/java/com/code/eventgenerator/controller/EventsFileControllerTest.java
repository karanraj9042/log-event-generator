package com.code.eventgenerator.controller;

import com.code.eventgenerator.model.Event;
import com.code.eventgenerator.service.EventFileService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RunWith(MockitoJUnitRunner.class)
public class EventsFileControllerTest {

    @InjectMocks
    private EventsFileController eventsFileController;

    @Mock
    private EventFileService eventFileService;

    @Test
    public void testEvent() {
        try {
            Mockito.when(eventFileService.saveEvents()).thenReturn(new CompletableFuture<>());
            CompletableFuture<ResponseEntity> responseEntityCompletableFuture = eventsFileController.getAllFilteredEvents();
            Assert.assertNotNull(responseEntityCompletableFuture);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testGetLargestEventByDuration() {
        try {
            Mockito.when(eventFileService.getEventJsonFromFile()).thenReturn(getMockEvent());
            ResponseEntity responseEntity = eventsFileController.getLargestEventByDuration();
            Assert.assertNotNull(responseEntity.getStatusCode());
            Assert.assertEquals(responseEntity.getStatusCodeValue(),  200);
            Assert.assertNotNull(responseEntity.getBody());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private List<Event> getMockEvent() {
        Event e1 = new Event();
        e1.setEventId("mock1");
        e1.setHost("s1");
        e1.setEventDuration(3L);
        e1.setTimestamp(3124566L);

        Event e2 = new Event();
        e1.setEventId("mock2");
        e1.setHost("s2");
        e1.setEventDuration(4L);
        e1.setTimestamp(3124567L);

        List<Event> events = new ArrayList<>();
        events.add(e1);
        events.add(e2);
        return events;

    }
}
