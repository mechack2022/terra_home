package com.fragile.terra_home.entities;

import com.fragile.terra_home.constants.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Goer {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String firstName;

    private String email;
    private UserRole role;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true )
    @JoinColumn(name="ticket_id")
    private List<Ticket> ticketList = new ArrayList<>();

    private LocalDateTime createdAt;


}
