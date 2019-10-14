package com.code.eventgenerator.service;

import com.code.eventgenerator.model.Event;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public interface EventFileService {
   CompletableFuture<Iterable<Event>> saveEvents() throws SQLException, IOException;
   List getEventJsonFromFile() throws IOException;
}
