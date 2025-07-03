package com.bondarenko.movieland.service.security;

import com.bondarenko.movieland.entity.dto.UserDetails;

public interface TokenService {
    String generateToken(UserDetails userDetails);

    Long getExpirationMillis(String token);

    void validateToken(String token);
}
