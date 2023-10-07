package com.fragile.terra_home.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="tickets_bought")
public class TicketBought {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String email;
    private Integer noOfTickets;
    private BigDecimal ticketTotalAmount;
    @ManyToOne
    private Ticket ticketId;
    @ManyToOne
    private Goer goer;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<PaymentInfo> ticketPaymentInfo = new ArrayList<>();
    private LocalDateTime createdAt;


}
