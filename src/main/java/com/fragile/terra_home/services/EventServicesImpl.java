package com.fragile.terra_home.services;


import com.fragile.terra_home.constants.EventStatus;
import com.fragile.terra_home.dto.request.CreateEventRequest;

import com.fragile.terra_home.dto.request.TicketDto;
import com.fragile.terra_home.entities.*;
import com.fragile.terra_home.exceptions.ResourceNotFoundException;
import com.fragile.terra_home.exceptions.UserException;
import com.fragile.terra_home.repository.CategoryRepository;
import com.fragile.terra_home.repository.EventRepository;
import com.fragile.terra_home.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServicesImpl implements EventServices {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final TicketServices ticketServices;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Event createEvent(User user, CreateEventRequest createEventRequest) {
        try {
            Event newEvent = createEventEntity(user, createEventRequest);
            Set<Ticket> ticketList = createTickets(newEvent, createEventRequest.getTickets());
            newEvent.setTickets(ticketList);
            return eventRepository.save(newEvent);
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Event> getAllEvent() {
        return eventRepository.findAll();
    }

    @Override
    public List<Event> getEventsByCreator(User user) {
        return eventRepository.findByEventCreator(user);
    }


    @Override
//    @Transactional
    public Event updateEvent(Long eventId, User user, CreateEventRequest createEventRequest) {
        try {
            log.info("Inside update implementation");
            Event foundEvent = getEventById(eventId);
            log.info("event found by Id : {} ", foundEvent.getEventCreator());
            // check the current user for the creator of event
            checkEventOwner(foundEvent, user);
            Event e = updateEventFields(foundEvent, createEventRequest);
            return eventRepository.save(e);
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server error: " + ex.getMessage(), ex);
        }

    }

    @Override
    public Event getEventById(Long eventId) {
        try {
            Optional<Event> event = eventRepository.findById(eventId);
            if (event.isPresent()) {
                return event.get();
            }
            throw new ResourceNotFoundException("Event with this id not found : " + eventId, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server error: " + ex.getMessage(), ex);
        }

    }

    @Override
    public List<Event> filterEvent(String categoryName, String location, LocalDateTime date) {
        try {
            log.info("Filtering events with categoryName: {}, location: {}, date: {}", categoryName, location, date);
            List<Event> filterEvent = eventRepository.filterEventsByCategoryLocationDate(categoryName, location, date);
            log.info("Filtered events: {}", filterEvent);
            return filterEvent;
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error: " + ex.getMessage(), ex);
        }
    }


    private Event findEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with the id: " + eventId));
    }

    private void checkEventOwner(Event event, User user) {
        if (!event.getEventCreator().equals(user)) {
            throw new UserException("You are not allowed to update this event");
        }
    }

    private Event updateEventFields(Event event, CreateEventRequest createEventRequest) {
        event.setName(createEventRequest.getName());
        event.setDescription(createEventRequest.getDescription());
        event.setEventImage("updatedImage.png");
        event.setLocation(createEventRequest.getLocation());
        event.setStartTime(createEventRequest.getStartTime());
        event.setEndTime(createEventRequest.getEndTime());
        event.setStartDate(createEventRequest.getStartDate());
        event.setEndDate(createEventRequest.getEndDate());
        event.setUpdatedAt(LocalDateTime.now());

        Set<Ticket> ticketSet = createEventRequest.getTickets()
                .stream()
                .map(t -> modelMapper.map(t, Ticket.class))
                .collect(Collectors.toSet());

        event.setTickets(ticketSet);
        return event;
    }


    private Event createEventEntity(User user, CreateEventRequest createEventRequest) {
        EventCategory category = categoryRepository.findByCategoryName(createEventRequest.getCategoryName())
                .orElseGet(() -> {
                    EventCategory newCategory = new EventCategory();
                    newCategory.setCategoryName(createEventRequest.getCategoryName());
                    return categoryRepository.save(newCategory);
                });

        Event newEvent = new Event();
        newEvent.setName(createEventRequest.getName());
        newEvent.setDescription(createEventRequest.getDescription());
        newEvent.setEventImage("eventImage.png");
        newEvent.setEventCreator(user);
        newEvent.setCategory(category);
        newEvent.setEventStatus(EventStatus.ACTIVE);
        newEvent.setLocation(createEventRequest.getLocation());
        newEvent.setStartTime(createEventRequest.getStartTime());
        newEvent.setEndTime(createEventRequest.getEndTime());
        newEvent.setStartDate(createEventRequest.getStartDate());
        newEvent.setEndDate(createEventRequest.getEndDate());
        newEvent.setCreateAt(LocalDateTime.now());

        return eventRepository.save(newEvent);
    }

    private Set<Ticket> createTickets(Event event, Set<TicketDto> ticketDtos) {
        Set<Ticket> ticketList = new HashSet<>();
        for (TicketDto ticketDto : ticketDtos) {
            Ticket newTicket = new Ticket();
            newTicket.setEvent(event);
            newTicket.setAvailableTicket(ticketDto.getAvailableTicket());
            newTicket.setTicketPrice(ticketDto.getTicketPrice());
            newTicket.setTicketType(ticketDto.getTicketType());
            newTicket.setIsSold(false);
            ticketList.add(newTicket);
        }

        if (ticketList.isEmpty()) {
            throw new IllegalArgumentException("At least one ticket must be provided.");
        }

        ticketRepository.saveAll(ticketList);
        return ticketList;
    }


}
