package com.fragile.terra_home.entities;

import com.fragile.terra_home.constants.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="goer_payment_log")
public class GoerPaymentLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "transaction_reference")
    private String transactionReference;

    @Column(name = "transaction_date")
    private String transactionDate;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "transaction_status", length = 50)
    @Builder.Default
    private TransactionStatus transactionStatus = TransactionStatus.PENDING;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Goer goer;

}
