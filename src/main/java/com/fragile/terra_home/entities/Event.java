package com.fragile.terra_home.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fragile.terra_home.constants.EventStatus;
import jakarta.persistence.*;
import jdk.jfr.Category;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
//@ToString
@Getter
@Setter
@Builder
@Entity
@Table(name="events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String location;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;
    @ManyToOne
    @JoinColumn(name="category_id")
    private EventCategory category;
    private String eventImage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    @ManyToOne
    @JoinColumn(name = "event_creator_id")
    private User eventCreator;

    @JoinColumn(name = "ticket_id")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Ticket> tickets = new HashSet<>();

    private LocalDateTime createAt;

    private LocalDateTime updatedAt;
}
