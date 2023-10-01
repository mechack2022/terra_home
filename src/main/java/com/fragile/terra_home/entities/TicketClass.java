package com.fragile.terra_home.entities;

import com.fragile.terra_home.constants.TicketType;
import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Setter
@Getter
@RequiredArgsConstructor
public class TicketClass {
    private Integer availableTicket;
    private Double ticketPrice;
    @Enumerated(EnumType.STRING)
    private TicketType ticketClass;
}
