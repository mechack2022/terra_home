package com.fragile.terra_home.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
//@ToString
@Table(name="users")
public class User {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String firstName;

    private String lastName;

    private String password;

    private String email;

    private UserRole role;
    @Column(unique = true)
    private String mobileNumber;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "event_id")
    private List<Event> eventList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="account_Id")
    private BeneficiaryAccount creatorAccount;

    private LocalDateTime createdAt;


}
