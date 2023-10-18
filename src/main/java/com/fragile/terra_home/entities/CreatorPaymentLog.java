//package com.fragile.terra_home.entities;
//
//import com.fragile.terra_home.constants.TransactionStatus;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.math.BigDecimal;
//
//@Entity
//@Setter
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class CreatorPaymentLog {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private String id;
//    @Column(name = "amount")
//    private BigDecimal amount;
//
//    @Column(name = "transaction_reference")
//    private String transactionReference;
//
//    @Column(name = "transaction_date")
//    private String transactionDate;
//
//    @Enumerated(value = EnumType.STRING)
//    @Column(name = "transaction_status", length = 50)
//    @Builder.Default
//    private TransactionStatus transactionStatus = TransactionStatus.PENDING;
//
//    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
//    private Goer goer;
//
//}
