package com.code.eventgenerator.comparator;

import com.code.eventgenerator.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

public class EventSortByDuration implements Comparator<Event> {
    private static final Logger logger = LoggerFactory.getLogger(EventSortByDuration.class);

    @Override
    public int compare(Event firstEvent, Event secondEvent) {
        logger.info("Sorting events log by duration",firstEvent.getEventDuration(),secondEvent.getEventDuration());
        return Long.compare(firstEvent.getEventDuration(),secondEvent.getEventDuration());
    }
}
