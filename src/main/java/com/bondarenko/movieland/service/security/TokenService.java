package com.bondarenko.movieland.service.security;

import com.bondarenko.movieland.service.security.dto.JwtTokens;
import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {
    JwtTokens generateToken(UserDetails userDetails);

    Long getExpirationMillis(String token);
    String extractEmailFromToken(String token);
    boolean isTokenValid(String token, UserDetails userDetails);

    boolean isRefreshToken(String token);
}
