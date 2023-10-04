package com.fragile.terra_home.dto.response;

import com.fragile.terra_home.constants.TicketType;
import com.fragile.terra_home.entities.Event;
import com.fragile.terra_home.entities.Goer;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketResponseDto {

    private Long  id;
    private Integer availableTicket;
    private Double ticketPrice;
    private TicketType ticketType;
    private Boolean isSold;

}
