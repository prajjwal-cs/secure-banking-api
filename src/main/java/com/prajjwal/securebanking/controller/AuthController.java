package com.prajjwal.securebanking.controller;

import com.prajjwal.securebanking.dto.AuthResponseDto;
import com.prajjwal.securebanking.dto.LoginRequestDto;
import com.prajjwal.securebanking.dto.RegistrationDto;
import com.prajjwal.securebanking.model.RefreshToken;
import com.prajjwal.securebanking.security.JwtProvider;
import com.prajjwal.securebanking.service.AuthService;
import com.prajjwal.securebanking.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtProvider jwtProvider) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest.getUsername(),
                loginRequest.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationDto request) {
        authService.register(request);

        return ResponseEntity.ok("User registered successfully.");
    }

    @PostMapping("/refresh")
    public AuthResponseDto refreshToken(@RequestBody String refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenService
                .getRefreshTokenByToken(refreshTokenRequest);

        refreshTokenService.verifyExpiration(refreshToken);

        String newAccessToken = jwtProvider.generateTokenFromUsername(
                refreshToken.getUser().getUsername()
        );

        return new AuthResponseDto(newAccessToken, refreshToken.getToken());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication) {
        String username = authentication.getName();
        authService.logout(username);

        return ResponseEntity.ok("Logged out successfully");
    }
}