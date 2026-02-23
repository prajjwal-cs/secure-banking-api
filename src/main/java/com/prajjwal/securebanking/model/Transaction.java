package com.prajjwal.securebanking.model;

import com.prajjwal.securebanking.model.enums.TransactionStatus;
import com.prajjwal.securebanking.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String transactionId;

    @ManyToOne
    @JoinColumn(name = "from_account_number")
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account_number")
    private Account toAccount;

    private Double amount;

    @Enumerated(value = EnumType.STRING)
    private TransactionType type;

    @Enumerated(value = EnumType.STRING)
    private TransactionStatus status;

    private Instant createdAt;
}