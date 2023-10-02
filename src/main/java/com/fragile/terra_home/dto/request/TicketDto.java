package com.fragile.terra_home.dto.request;

import com.fragile.terra_home.constants.TicketType;
import com.fragile.terra_home.entities.Event;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDto {
    @NotNull(message="Number of Ticket available required")
    private Integer availableTicket;
    @NotNull(message="Ticket price is require")
    private Double ticketPrice;

    @NotNull(message="TicketType required")
    private TicketType ticketType;


}
