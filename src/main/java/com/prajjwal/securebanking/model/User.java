package com.prajjwal.securebanking.model;

import com.prajjwal.securebanking.model.enums.Roles;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    // ADMIN OR USER
    @Enumerated(value = EnumType.STRING)
    private Roles role;

    private boolean isActive;

    @CreationTimestamp
    private LocalDateTime createdAt;

}