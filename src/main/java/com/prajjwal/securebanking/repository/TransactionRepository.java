package com.prajjwal.securebanking.repository;

import com.prajjwal.securebanking.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    List<Transaction> findByFromAccountAccountNumberOrToAccountAccountNumber(
            String fromAccount, String toAccountNumber
    );
}