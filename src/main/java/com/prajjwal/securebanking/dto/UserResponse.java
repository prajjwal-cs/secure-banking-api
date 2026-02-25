package com.prajjwal.securebanking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private String username;
    private String email;
    private String name;
    private String role;
    private LocalDateTime createdAt;
}