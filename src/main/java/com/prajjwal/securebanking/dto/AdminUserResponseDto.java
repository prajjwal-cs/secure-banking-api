package com.prajjwal.securebanking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminUserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String role;
    private boolean accountNonLocked;
    private int failedAttempts;
}