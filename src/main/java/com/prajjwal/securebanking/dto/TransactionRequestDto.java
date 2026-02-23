package com.prajjwal.securebanking.dto;

import lombok.Data;

@Data
public class TransactionRequestDto {
    private String fromAccount;
    private String toAccount;
    private Double amount;
}