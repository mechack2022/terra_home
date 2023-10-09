package com.fragile.terra_home.services;

import com.fragile.terra_home.constants.ApiConstant;
import com.fragile.terra_home.constants.UserRole;
import com.fragile.terra_home.dto.request.BuyTicketRequestDto;
import com.fragile.terra_home.dto.response.ApiResponse;
import com.fragile.terra_home.dto.response.TicketResponseDto;
import com.fragile.terra_home.entities.*;
import com.fragile.terra_home.exceptions.ResourceNotFoundException;
import com.fragile.terra_home.repository.EventRepository;
import com.fragile.terra_home.repository.GoerRepository;
import com.fragile.terra_home.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketServices {
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final CreatorService creatorService;
    private final ModelMapper modelMapper;

    private final GoerRepository goerRepository;

    @Override
    public List<TicketResponseDto> findCreatorTicketsByEventId(User user, Long eventId) {
        try {
            if (eventRepository.findById(eventId).isEmpty()) {
                throw new ResourceNotFoundException("Event not found with ID: " + eventId, HttpStatus.BAD_REQUEST);
            }
            // Find the creator by email
            User creator = creatorService.findUserByEmail(user.getEmail());
            // Handle User Not Found
            if (creator == null) {
                throw new UsernameNotFoundException("User not found with email: " + user.getEmail());
            }
            // Get the events created by the creator
            List<Event> creatorEvents = eventRepository.findByEventCreator(creator);
            // Filter the events to find the one with the given eventId
            Optional<Event> eventOptional = creatorEvents.stream().filter(event -> Objects.equals(event.getId(), eventId)).findFirst();
            if (eventOptional.isEmpty()) {
                throw new ResourceNotFoundException("Event not found with ID: " + eventId);
            }
            // Get the tickets associated with the found event
            Event foundEvent = eventOptional.get();
            List<Ticket> foundEventTickets = ticketRepository.findByEvent(foundEvent);
            log.info("found Event tickets: {}", foundEventTickets);
            return foundEventTickets.stream().map(ticket -> modelMapper.map(ticket, TicketResponseDto.class)).collect(Collectors.toList());
        } catch (ResourceNotFoundException e) {
            // Handle Event Not Found Exception
            throw new ResourceNotFoundException("Event not found: " + e.getMessage(), HttpStatus.BAD_REQUEST);
//            throw e;
        } catch (UsernameNotFoundException e) {
            // Handle User Not Found Exception
            throw new UsernameNotFoundException("User not found with email: " + e.getMessage(), e);
        } catch (Exception e) {
            // Handle other exceptions (e.g., network errors)
            log.error("An unexpected error occurred: " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred.");
        }
    }

    @Override
    public ResponseEntity<ApiResponse<?>> saveUserTicket(Long eventId, Long ticketId, BuyTicketRequestDto req) {
        try {
//           is event exist, get event tickets
            Optional<Event> event = eventRepository.findById(eventId);
            if (event.isPresent()) {
                // get te event tickets
                Set<Ticket> eventTickets = event.get().getTickets();
                if (eventTickets.isEmpty()) {
                    throw new ResourceNotFoundException("No ticket was created for this event", HttpStatus.BAD_REQUEST);
                }
//              filter the ticket that has the same id as the request ticket id
                Optional<Ticket> matchingTicket = eventTickets.stream().filter(ticket -> Objects.equals(ticket.getId(), ticketId)).findFirst();
                if (matchingTicket.isPresent()) {
//                    check the number of available tickets, if is up to the number request
                    if (req.getNoOfTickets() <= matchingTicket.get().getAvailableTicket()) {
//                        create a new  goer
                        Goer goer = createGoer(req, matchingTicket.get());
                        // reduce the number of available tickets
                        Integer availableNoOfTicket = reduceAvailableTicket(goer.getTicket().getId(), eventTickets, req.getNoOfTickets());
                        Optional<Ticket> foundTicket = ticketRepository.findById(matchingTicket.get().getId());

                        if (foundTicket.isEmpty()) {
                            throw new ResourceNotFoundException("No ticket id match this id: " + matchingTicket.get().getId());
                        }
                        Ticket t = foundTicket.get();
                        t.setAvailableTicket(availableNoOfTicket);
                        ticketRepository.save(t);
                        //save the goer information
                        Goer savedGoer = goerRepository.save(goer);

                        return new ResponseEntity<>(ApiResponse.builder().message(ApiConstant.IS_SUCCESS).status(true).data(savedGoer).build(), HttpStatus.OK);
                    }
                    throw new ResourceNotFoundException("The number of tickets you requested for is more that whats is available, number if available ticket is : " + matchingTicket.get().getAvailableTicket());
                }
                throw new ResourceNotFoundException("Ticket not found with id : " + ticketId);
            }
            throw new ResourceNotFoundException("Event not found with id : " + eventId);
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server error: " + ex.getMessage(), ex);
        }

    }

    private Goer createGoer(BuyTicketRequestDto req, Ticket ticket) {
        Goer goer = new Goer();
        goer.setRole(UserRole.GOER);
        goer.setFirstName(req.getFirstName());
        goer.setEmail(req.getEmail());
        goer.setNoOfTickets(req.getNoOfTickets());
        BigDecimal totalAmount = BigDecimal.valueOf(ticket.getTicketPrice() * req.getNoOfTickets());
        goer.setTicketTotalAmount(totalAmount);
        goer.setCreatedAt(LocalDateTime.now());
        goer.setTicket(ticket);
        return goer;
    }


    private Integer reduceAvailableTicket(Long ticketId, Set<Ticket> tickets, Integer noOfTicket) {
        Integer availableTicket = ticketRepository.findById(ticketId).get().getAvailableTicket();
        for (Ticket t : tickets) {
            if (Objects.equals(ticketId, t.getId())) {
                availableTicket = t.getAvailableTicket() - noOfTicket;
            }
        }
        return availableTicket;
    }


}
