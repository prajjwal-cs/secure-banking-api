package com.prajjwal.securebanking.dto;

import com.prajjwal.securebanking.model.enums.TransactionStatus;
import com.prajjwal.securebanking.model.enums.TransactionType;
import lombok.Data;

import java.time.Instant;

@Data
public class TransactionFilterRequestDto {
    private String accountNumber;
    private TransactionType type;
    private TransactionStatus status;
    private Instant fromDate;
    private Instant toDate;
}