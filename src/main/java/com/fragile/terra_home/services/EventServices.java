package com.fragile.terra_home.services;

import com.fragile.terra_home.dto.request.CreateEventRequest;
import com.fragile.terra_home.entities.Event;
import com.fragile.terra_home.entities.User;

import java.time.LocalDateTime;
import java.util.List;

public interface EventServices {

    Event createEvent(User user, CreateEventRequest createEventRequest);

    List<Event> getAllEvent();

    List<Event> getEventsByCreator(User user);


    Event updateEvent(Long eventId, User user, Event createEventRequest);

    String deletedEvent(User user, Long eventId);

    Event getEventById(Long eventId);

    List<Event>  filterEvent(String categoryName, String location, LocalDateTime date);
}
