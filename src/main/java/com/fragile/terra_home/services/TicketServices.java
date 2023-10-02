package com.fragile.terra_home.services;

import com.fragile.terra_home.dto.request.TicketDto;
import com.fragile.terra_home.entities.Ticket;

import java.util.List;

public interface TicketServices {
//    Ticket createTicket(TicketDto req);

    List<Ticket> findTicketByEvent(Long eventId);
}
