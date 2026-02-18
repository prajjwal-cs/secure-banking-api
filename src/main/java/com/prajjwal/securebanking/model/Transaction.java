package com.prajjwal.securebanking.model;

import com.prajjwal.securebanking.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "from_account_number")
    private Account fromAccount;

    @OneToOne
    @JoinColumn(name = "to_account_number")
    private Account toAccount;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TransactionType type;

    @Column(nullable = false)
    private String status;

    private LocalDateTime createdAt;
}