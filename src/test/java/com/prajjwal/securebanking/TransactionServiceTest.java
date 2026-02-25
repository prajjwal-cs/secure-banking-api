package com.prajjwal.securebanking;

import com.prajjwal.securebanking.dto.TransactionResponseDto;
import com.prajjwal.securebanking.model.Account;
import com.prajjwal.securebanking.model.enums.AccountStatus;
import com.prajjwal.securebanking.repository.AccountRepository;
import com.prajjwal.securebanking.repository.TransactionRepository;
import com.prajjwal.securebanking.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Account account;

    @BeforeEach
    void setup() {
        account = new Account();
        account.setAccountNumber("AC123");
        account.setBalance(1000.00);
        account.setStatus(AccountStatus.ACTIVE);
    }

    @Test
    void deposit() {
        when(accountRepository.findByAccountNumber("AC123")).thenReturn(Optional.of(account));

        TransactionResponseDto response = transactionService.deposit("AC123", 500.0);

        assertEquals(1500.0, account.getBalance());
        assertEquals("DEPOSIT", response.getType());
    }

    @Test
    void withdraw() {
        when(accountRepository.findByAccountNumber("AC123")).thenReturn(Optional.of(account));

        assertThrows(IllegalStateException.class,
                () -> transactionService.withdraw("AC123", 2000.0));
    }
}