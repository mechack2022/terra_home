package com.fragile.terra_home.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fragile.terra_home.constants.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@ToString
@Table(name="goers")
public class Goer {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private String firstName;
    private String email;
    private UserRole role;
    private BigDecimal ticketTotalAmount;
    private Integer noOfTickets;
    @ManyToOne
    @JoinColumn(name="ticket_id")
    private Ticket ticket;
    private Boolean isPaid = false;
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "goer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PaymentLog> paymentLogId = new ArrayList<>();


}
