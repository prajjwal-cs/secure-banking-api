package com.prajjwal.securebanking.service;

import com.prajjwal.securebanking.dto.ChangePasswordRequestDto;
import com.prajjwal.securebanking.dto.UserResponse;
import com.prajjwal.securebanking.exception.InvalidCredentialsException;
import com.prajjwal.securebanking.exception.ResourceNotFoundException;
import com.prajjwal.securebanking.model.Account;
import com.prajjwal.securebanking.model.User;
import com.prajjwal.securebanking.repository.AccountRepository;
import com.prajjwal.securebanking.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
    }

    public UserResponse getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public void changePassword(String username, ChangePasswordRequestDto request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }

    public List<Account> getListOfAccounts(String username) {
        return accountRepository.findByUserUsername(username);
    }
}