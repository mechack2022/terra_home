package com.fragile.terra_home.services;

import com.fragile.terra_home.dto.response.TicketResponseDto;
import com.fragile.terra_home.entities.*;
import com.fragile.terra_home.exceptions.DatabaseException;
import com.fragile.terra_home.exceptions.ResourceNotFoundException;
import com.fragile.terra_home.repository.EventRepository;
import com.fragile.terra_home.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketServices {
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final CreatorService creatorService;

    private final ModelMapper modelMapper;
    @Override
    public List<TicketResponseDto> findCreatorTicketsByEventId(User user, Long eventId) {
        try {
            // Find the creator by email
            User creator = creatorService.findUserByEmail(user.getEmail());
            // Handle User Not Found
            if (creator == null) {
                throw new UsernameNotFoundException("User not found with email: " + user.getEmail());
            }
            // Get the events created by the creator
            List<Event> creatorEvents = eventRepository.findByEventCreator(creator);
            // Filter the events to find the one with the given eventId
            Optional<Event> eventOptional = creatorEvents.stream()
                    .filter(event -> Objects.equals(event.getId(), eventId))
                    .findFirst();
            if (eventOptional.isEmpty()) {
                throw new ResourceNotFoundException("Event not found with ID: " + eventId);
            }
            // Get the tickets associated with the found event
            Event foundEvent = eventOptional.get();
            List<Ticket> foundEventTickets =  ticketRepository.findByEvent(foundEvent);
            log.info("found Event tickets: {}", foundEventTickets);
            return foundEventTickets.stream().map(ticket -> modelMapper.map(ticket, TicketResponseDto.class)).collect(Collectors.toList());
        } catch (UsernameNotFoundException e) {
            // Handle User Not Found Exception
            log.error("User not found: " + e.getMessage(), e);
            throw e;
        } catch (ResourceNotFoundException e) {
            // Handle Event Not Found Exception
            log.error("Event not found: " + e.getMessage(), e);
            throw e;
        } catch (DataAccessException e) {
            // Handle Database Error
            log.error("Database error: " + e.getMessage(), e);
            throw new DatabaseException("An error occurred while accessing the database.");
        } catch (Exception e) {
            // Handle other exceptions (e.g., network errors)
            log.error("An unexpected error occurred: " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred.");
        }
    }


//    public void saveTicket(SaveTicketRequestDto req){
//        try{
////           is event exist, get event tickets
//            Optional<Event> event = eventRepository.findById(req.getEventId());
//            if(event.isPresent()){
//                Set<Ticket> userTickets = new HashSet<>();
//                 List<Ticket> eventTickets = findTicketByEvent(req.getEventId());
////              if goer already exist, get his or her ticket list and add the new ticket
//                Optional<Goer> foundGoer = goerRepository.findByEmail(req.getEmail());
//                if(foundGoer.isPresent()){
//
//                }
//            }
//        }catch (Exception ex){
//            throw new RuntimeException("Internal Server error: " + ex.getMessage(), ex);
//        }
//
//    }
//
//  private Ticket getTicketById(Set<Ticket> tickets, Long ticketId){
//
//  }

}
