package com.bondarenko.movieland.service.security;

public interface TokenService {
    String generateToken(String email);
    boolean validateToken(String token);
    String extractExpireDate(String token);
}
