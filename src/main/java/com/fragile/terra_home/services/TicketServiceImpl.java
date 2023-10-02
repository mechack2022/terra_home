package com.fragile.terra_home.services;

import com.fragile.terra_home.dto.request.TicketDto;
import com.fragile.terra_home.dto.request.TicketClassDto;
import com.fragile.terra_home.entities.Event;
import com.fragile.terra_home.entities.Ticket;
import com.fragile.terra_home.entities.TicketClass;
import com.fragile.terra_home.exceptions.ResourceNotFoundException;
import com.fragile.terra_home.repository.EventRepository;
import com.fragile.terra_home.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketServices {

    public final EventRepository eventRepository;

    public final TicketRepository ticketRepository;

//    @Override
//    public Ticket createTicket(TicketDto req) {
//        try {
//            Optional<Event> event = eventRepository.findById(req.getEventId());
//            if (event.isEmpty()) {
//                throw new ResourceNotFoundException("Event not found with the id: " + req.getEventId());
//            }
//            // Create a new Ticket
//            Ticket newTicket = new Ticket();
//            newTicket.setEvent(event.get());
//            // Populate TicketClass information based on the provided TicketClassDto objects
//            Set<TicketClass> ticketClassList = new HashSet<>();
//
//            for (TicketClassDto ticketClassDto : req.getTicketClasses()) {
//                TicketClass newTicketClass = new TicketClass();
//                newTicketClass.setAvailableTicket(ticketClassDto.getAvailableTicket());
//                newTicketClass.setTicketPrice(ticketClassDto.getTicketPrice());
//                newTicketClass.setTicketClass(ticketClassDto.getTicketClass());
//                ticketClassList.add(newTicketClass);
//            }
//            newTicket.setTicketClassList(ticketClassList);
//            // Save the new Ticket
//            return ticketRepository.save(newTicket);
//        } catch (Exception ex) {
//            throw new RuntimeException("Internal Server error: " + ex.getMessage(), ex);
//        }
//    }

    @Override
    public List<Ticket> findTicketByEvent(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isEmpty()) {
            throw new ResourceNotFoundException("Event not found with ID: " + eventId);
        }
        return ticketRepository.findTicketsByEventId(eventId);
    }


}
