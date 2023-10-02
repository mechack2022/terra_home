package com.fragile.terra_home.services;


import com.fragile.terra_home.constants.EventStatus;
import com.fragile.terra_home.dto.request.CreateEventRequest;

import com.fragile.terra_home.dto.request.TicketDto;
import com.fragile.terra_home.entities.*;
import com.fragile.terra_home.repository.CategoryRepository;
import com.fragile.terra_home.repository.EventRepository;
import com.fragile.terra_home.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EventServicesImpl implements EventServices {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final TicketServices ticketServices;

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
    public List<Event> getAllEvent(){
        return eventRepository.findAll();
    }

    @Override
    public List<Event> getEventByCreator(User user){
        return eventRepository.findByEventCreator(user);
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
            ticketList.add(newTicket);
        }

        if (ticketList.isEmpty()) {
            throw new IllegalArgumentException("At least one ticket must be provided.");
        }

        ticketRepository.saveAll(ticketList);
        return ticketList;
    }


}
