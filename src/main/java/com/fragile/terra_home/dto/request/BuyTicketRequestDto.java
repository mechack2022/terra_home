package com.fragile.terra_home.dto.request;

import com.fragile.terra_home.entities.Goer;
import com.fragile.terra_home.entities.Ticket;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyTicketRequestDto {
    private Long id;
    private String firstName;
    private String email;
    private Integer noOfTickets;
    private BigDecimal ticketTotalAmount;

}
