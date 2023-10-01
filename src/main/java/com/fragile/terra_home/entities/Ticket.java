package com.fragile.terra_home.entities;

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
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @ElementCollection
    @Column(name = "ticketClass")
    private Set<TicketClass> ticketClassList = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "goer_id")
    private Goer goer;



}
