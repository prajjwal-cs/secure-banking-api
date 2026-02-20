package com.prajjwal.securebanking.repository;

import com.prajjwal.securebanking.model.Account;
import com.prajjwal.securebanking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByUserUsername(String username);
}
