package com.prajjwal.securebanking.service;

import com.prajjwal.securebanking.model.RefreshToken;
import com.prajjwal.securebanking.model.User;
import com.prajjwal.securebanking.repository.RefreshTokenRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryTime(Instant.now().plusSeconds(24 * 60 * 60));

        return refreshTokenRepository.save(refreshToken);
    }

    public void verifyExpiration(RefreshToken token) {
        if (token.getExpiryTime().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new BadCredentialsException("Refresh token expired");
        }
    }

    public RefreshToken getRefreshTokenByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
    }

    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}