package com.prajjwal.securebanking.controller;

import com.prajjwal.securebanking.model.Account;
import com.prajjwal.securebanking.model.enums.AccountType;
import com.prajjwal.securebanking.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/bank/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody AccountType type,
                                                 @RequestBody Double amount,
                                                 Authentication authentication) {
        String username = authentication.getName();
        Account account = accountService.createAccount(username, type, amount);

        return ResponseEntity.ok(account);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber,
                                              Authentication authentication) throws AccessDeniedException {
        String username = authentication.getName();
        Account account = accountService.getAccount(accountNumber, username);

        return ResponseEntity.ok(account);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/block")
    public ResponseEntity<String> blockAccount(@PathVariable Long id) {
        accountService.blockAccount(id);
        return ResponseEntity.ok("Account blocked successfully!");
    }
}