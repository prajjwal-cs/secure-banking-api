package com.prajjwal.securebanking.service;

import com.prajjwal.securebanking.dto.AuthResponseDto;
import com.prajjwal.securebanking.dto.RegistrationDto;
import com.prajjwal.securebanking.exception.InvalidCredentialsException;
import com.prajjwal.securebanking.exception.UserAlreadyExistException;
import com.prajjwal.securebanking.model.RefreshToken;
import com.prajjwal.securebanking.model.User;
import com.prajjwal.securebanking.model.enums.Roles;
import com.prajjwal.securebanking.repository.UserRepository;
import com.prajjwal.securebanking.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Log4j2
@Service
public class AuthService {

    private static final int MAX_FAILED_ATTEMPTS = 5;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       RefreshTokenService refreshTokenService,
                       AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    public AuthResponseDto login(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            resetFailedAttempts(username);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("User {} successfully authenticated", username);

            String accessToken = jwtProvider.generateToken(authentication);

            User user = userRepository.findByUsername(username).orElseThrow(() ->
                    new UsernameNotFoundException("User not found"));

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

            return new AuthResponseDto(accessToken, refreshToken.getToken());
        } catch (BadCredentialsException ex) {
            increaseFailedAttempts(username);
            log.warn("Invalid credentials for user {}", username);
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    private void resetFailedAttempts(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        user.setFailedAttempts(0);
        user.setActive(true);
        user.setLockTime(null);

        userRepository.save(user);
    }

    private void increaseFailedAttempts(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        int newFailedCount = user.getFailedAttempts() + 1;
        user.setFailedAttempts(newFailedCount);

        if (newFailedCount >= MAX_FAILED_ATTEMPTS) {
            user.setActive(false);
            user.setLockTime(Instant.now());
        }

        userRepository.save(user);
    }

    @Transactional
    public void register(RegistrationDto registrationDto) {
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            log.warn("Registration failed. Username already exists: {}", registrationDto.getUsername());
            throw new UserAlreadyExistException("Username already taken.");
        }
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            log.warn("Registration failed. Email already exists: {}", registrationDto.getEmail());
            throw new UserAlreadyExistException("Email already exists.");
        }

        User user = new User();
        user.setName(registrationDto.getName());
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRole(Roles.USER);
        userRepository.save(user);
    }

    public void logout(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        refreshTokenService.deleteByUser(user);

        log.info("User {} logged out successfully !", username);
    }
}