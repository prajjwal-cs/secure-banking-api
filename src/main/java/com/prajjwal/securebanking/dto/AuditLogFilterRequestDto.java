package com.prajjwal.securebanking.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class AuditLogFilterRequestDto {
    private String username;
    private String action;
    private Instant fromDate;
    private Instant toDate;
}