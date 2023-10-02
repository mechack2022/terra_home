package com.fragile.terra_home.repository;

import com.fragile.terra_home.entities.Event;
import com.fragile.terra_home.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t WHERE t.event =: eventId")
    List<Ticket> findTicketsByEventId(Long eventId);


}
