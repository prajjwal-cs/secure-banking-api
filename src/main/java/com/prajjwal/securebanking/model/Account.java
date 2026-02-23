package com.prajjwal.securebanking.model;

import com.prajjwal.securebanking.model.enums.AccountStatus;
import com.prajjwal.securebanking.model.enums.AccountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(value = EnumType.STRING)
    private AccountType type;

    private double balance = 0.0;

    @Enumerated(value = EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;

    private LocalDateTime createdAt;
}