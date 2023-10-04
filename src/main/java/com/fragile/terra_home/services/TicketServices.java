package com.fragile.terra_home.services;

import com.fragile.terra_home.dto.request.TicketDto;
import com.fragile.terra_home.dto.response.TicketResponseDto;
import com.fragile.terra_home.entities.Ticket;
import com.fragile.terra_home.entities.User;

import java.util.List;

public interface TicketServices {
    List<TicketResponseDto> findCreatorTicketsByEventId(User user, Long eventId);
//    Ticket createTicket(TicketDto req);

}
