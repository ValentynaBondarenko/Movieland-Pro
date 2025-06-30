package com.bondarenko.movieland.service.security;

import com.bondarenko.movieland.api.model.UserRequest;

public interface TokenService {
    String generateToken(String email);
    boolean validateToken(String token);
    String extractExpireDate(String token);
}
