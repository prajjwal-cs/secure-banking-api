package com.prajjwal.securebanking.controller;

import com.prajjwal.securebanking.dto.TransactionRequestDto;
import com.prajjwal.securebanking.dto.TransactionResponseDto;
import com.prajjwal.securebanking.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDto> deposit(
            @RequestParam String accountNumber,
            @RequestParam Double amount) {
        return ResponseEntity.ok(transactionService.deposit(accountNumber, amount));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponseDto> withdraw(
            @RequestParam String fromAccount,
            @RequestParam Double amount) {
        return ResponseEntity.ok(transactionService.withdraw(fromAccount, amount));
    }

    public ResponseEntity<TransactionResponseDto> transfer(
            @RequestBody TransactionRequestDto transactionRequest) {
        return ResponseEntity.ok(transactionService.transfer(
                transactionRequest.getFromAccount(),
                transactionRequest.getToAccount(),
                transactionRequest.getAmount()
        ));
    }

}