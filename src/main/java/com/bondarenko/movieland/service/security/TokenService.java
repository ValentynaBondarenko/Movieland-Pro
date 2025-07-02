package com.bondarenko.movieland.service.security;

import com.bondarenko.movieland.entity.dto.UserDetails;


public interface TokenService {
    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);

    void validateToken(String token);
}
