package com.prajjwal.securebanking.controller;

import com.prajjwal.securebanking.dto.AuthResponseDto;
import com.prajjwal.securebanking.model.RefreshToken;
import com.prajjwal.securebanking.security.JwtProvider;
import com.prajjwal.securebanking.service.AuthService;
import com.prajjwal.securebanking.service.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
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