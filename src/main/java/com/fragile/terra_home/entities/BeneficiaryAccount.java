package com.fragile.terra_home.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BeneficiaryAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountHolderName;
    private String bankName;
    private String accountNumber;
    @OneToOne
    @JoinColumn(name = "creator_id")
    @JsonIgnore
    private User creator;


}

