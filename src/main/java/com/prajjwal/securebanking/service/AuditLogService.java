package com.prajjwal.securebanking.service;

import com.prajjwal.securebanking.model.AuditLog;
import com.prajjwal.securebanking.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(String username, String action, String accountNumber, String transactionId) {
        AuditLog audit = new AuditLog();
        audit.setUsername(username);
        audit.setAction(action);
        audit.setAccountNumber(accountNumber);
        audit.setTransactionId(transactionId);
        audit.setTimestamp(Instant.now());

        auditLogRepository.save(audit);
    }
}