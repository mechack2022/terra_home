package com.fragile.terra_home.entities;

import com.fragile.terra_home.constants.TicketType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name="tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    private Integer availableTicket;
    private Double ticketPrice;
    @Enumerated(EnumType.STRING)
    private TicketType ticketType;
    private Boolean isSold;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "goer_id")
    @ToString.Exclude
    private List<Goer> goers = new ArrayList<>();


}
