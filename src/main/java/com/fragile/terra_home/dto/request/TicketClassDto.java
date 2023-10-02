package com.fragile.terra_home.dto.request;

import com.fragile.terra_home.constants.TicketType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketClassDto {
    @NotNull(message="Number of Ticket available required")
    private Integer availableTicket;
    @NotNull(message="Ticket price is require")
    private Double ticketPrice;

    @NotNull(message="TicketType required")
    private TicketType ticketType;

}
