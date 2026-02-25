package com.prajjwal.securebanking.service;

import com.prajjwal.securebanking.dto.AdminUserResponseDto;
import com.prajjwal.securebanking.dto.AuditLogFilterRequestDto;
import com.prajjwal.securebanking.dto.TransactionFilterRequestDto;
import com.prajjwal.securebanking.exception.ResourceNotFoundException;
import com.prajjwal.securebanking.model.AuditLog;
import com.prajjwal.securebanking.model.Transaction;
import com.prajjwal.securebanking.model.User;
import com.prajjwal.securebanking.repository.AuditLogRepository;
import com.prajjwal.securebanking.repository.TransactionRepository;
import com.prajjwal.securebanking.repository.UserRepository;
import com.prajjwal.securebanking.service.spec.AuditLogSpecification;
import com.prajjwal.securebanking.service.spec.TransactionSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class AdminService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AuditLogRepository auditRepository;

    public AdminService(UserRepository userRepository,
                        TransactionRepository transactionRepository,
                        AuditLogRepository auditRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.auditRepository = auditRepository;
    }

    public List<AdminUserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> AdminUserResponseDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .accountNonLocked(user.isActive())
                        .failedAttempts(user.getFailedAttempts())
                        .build()
                ).toList();
    }

    public void lockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setActive(false);
        userRepository.save(user);
    }

    public void unlockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setActive(false);
        userRepository.save(user);
    }


    public Page<Transaction> getFilteredTransactions(TransactionFilterRequestDto request,
                                                     Pageable pageable) {
        Specification<Transaction> spec = TransactionSpecification.filter(request);

        return transactionRepository.findAll(spec, pageable);
    }

    public Page<AuditLog> getFilteredAuditLogs(AuditLogFilterRequestDto request,
                                               Pageable pageable) {
        Specification<AuditLog> specification = AuditLogSpecification.filter(request);

        return auditRepository.findAll(specification, pageable);
    }
}