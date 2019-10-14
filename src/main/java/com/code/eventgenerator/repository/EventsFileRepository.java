package com.code.eventgenerator.repository;

import com.code.eventgenerator.model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsFileRepository extends CrudRepository<Event,String> {
}
