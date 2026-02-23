package com.prajjwal.securebanking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Builder
@Data
public class TransactionResponseDto {
    private String transactionId;
    private String type;
    private Double amount;
    private String status;
    private Instant timestamp;
}