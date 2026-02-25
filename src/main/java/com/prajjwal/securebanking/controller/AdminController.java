package com.prajjwal.securebanking.controller;

import com.prajjwal.securebanking.dto.AdminUserResponseDto;
import com.prajjwal.securebanking.dto.AuditLogFilterRequestDto;
import com.prajjwal.securebanking.dto.TransactionFilterRequestDto;
import com.prajjwal.securebanking.model.AuditLog;
import com.prajjwal.securebanking.model.Transaction;
import com.prajjwal.securebanking.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponseDto>> getUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/users/{id}/lock")
    public ResponseEntity<String> lockUser(@PathVariable Long id) {
        adminService.lockUser(id);
        return ResponseEntity.ok("User {" + id + "}, locked.");
    }

    @PutMapping("users/{id}/unlock")
    public ResponseEntity<String> unlockUser(@PathVariable Long id) {
        adminService.unlockUser(id);
        return ResponseEntity.ok("User {" + id + "} unlocked.");
    }

    @GetMapping("/transactions")
    public ResponseEntity<Page<Transaction>> getTransactions(
            @RequestParam(required = false) TransactionFilterRequestDto filter,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(adminService.getFilteredTransactions(filter, pageable));
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<Page<AuditLog>> getAuditLogs(
            @RequestParam(required = false)AuditLogFilterRequestDto filter,
            @PageableDefault(size = 10, sort = "timestamp", direction = Sort.Direction.DESC)
            Pageable pageable
            ) {
        return ResponseEntity.ok(adminService.getFilteredAuditLogs(filter, pageable));
    }
}