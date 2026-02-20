package com.prajjwal.securebanking.service;

import com.prajjwal.securebanking.exception.ResourceNotFoundException;
import com.prajjwal.securebanking.model.Account;
import com.prajjwal.securebanking.model.User;
import com.prajjwal.securebanking.model.enums.AccountStatus;
import com.prajjwal.securebanking.model.enums.AccountType;
import com.prajjwal.securebanking.repository.AccountRepository;
import com.prajjwal.securebanking.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Account createAccount(String username, AccountType type, Double openingAmount) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setUser(user);
        account.setBalance(openingAmount);
        account.setType(type);
        account.setStatus(AccountStatus.ACTIVE);

        return accountRepository.save(account);
    }

    private String generateAccountNumber() {
        return "AC" + System.currentTimeMillis();
    }

    public Account getAccount(String accountNumber, String username) throws AccessDeniedException {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found."));

        if (!account.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You can not access this account.");
        }

        return account;
    }

    @Transactional
    public void blockAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found."));

        account.setStatus(AccountStatus.BLOCKED);
        accountRepository.save(account);
    }
}