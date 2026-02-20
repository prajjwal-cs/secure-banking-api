package com.prajjwal.securebanking.dto;

import com.prajjwal.securebanking.model.RefreshToken;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    private String accessToken;
    private String refreshToken;
    private final String TOKEN_TYPE = "Bearer";
}