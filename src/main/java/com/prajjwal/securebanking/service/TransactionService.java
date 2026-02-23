package com.prajjwal.securebanking.service;

import com.prajjwal.securebanking.dto.TransactionResponseDto;
import com.prajjwal.securebanking.exception.ResourceNotFoundException;
import com.prajjwal.securebanking.model.Account;
import com.prajjwal.securebanking.model.Transaction;
import com.prajjwal.securebanking.model.enums.AccountStatus;
import com.prajjwal.securebanking.model.enums.TransactionStatus;
import com.prajjwal.securebanking.model.enums.TransactionType;
import com.prajjwal.securebanking.repository.AccountRepository;
import com.prajjwal.securebanking.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public void validateAccount(Account account) {
        if (account.getStatus() == AccountStatus.BLOCKED) {
            throw new IllegalStateException("Account is blocked!");
        }
    }

    @Transactional
    public TransactionResponseDto deposit(String accountNumber, Double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found."));

        validateAccount(account);

        account.setBalance(account.getBalance() + amount);

        Transaction transaction = new Transaction();
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setToAccount(account);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setCreatedAt(Instant.now());

        transactionRepository.save(transaction);

        return mapToResponse(transaction);
    }

    @Transactional
    public TransactionResponseDto withdraw(String fromAccount, Double amount) {
        Account account = accountRepository.findByAccountNumberForUpdate(fromAccount)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found."));

        validateAccount(account);

        if (account.getBalance() < amount) {
            throw new IllegalStateException("Insufficient balance");
        }
        account.setBalance(account.getBalance() - amount);

        Transaction transaction = new Transaction();
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setFromAccount(account);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.WITHDRAW);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setCreatedAt(Instant.now());

        transactionRepository.save(transaction);

        return mapToResponse(transaction);
    }

    @Transactional
    public TransactionResponseDto transfer(String fromAccount, String toAccount, Double amount) {
        Account sender = accountRepository.findByAccountNumberForUpdate(fromAccount)
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found."));

        Account receiver = accountRepository.findByAccountNumberForUpdate(toAccount)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found."));

        validateAccount(sender);
        validateAccount(receiver);

        if (sender.getBalance() < amount) {
            throw new IllegalStateException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        Transaction transaction = new Transaction();
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setFromAccount(sender);
        transaction.setToAccount(receiver);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setCreatedAt(Instant.now());

        transactionRepository.save(transaction);

        return mapToResponse(transaction);
    }

    private TransactionResponseDto mapToResponse(Transaction transaction) {
        return TransactionResponseDto.builder()
                .transactionId(transaction.getTransactionId())
                .amount(transaction.getAmount())
                .type(transaction.getType().toString())
                .status(transaction.getStatus().toString())
                .timestamp(transaction.getCreatedAt())
                .build();
    }
}