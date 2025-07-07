package com.bondarenko.movieland.service.cache.security;

import com.bondarenko.movieland.service.annotation.CacheService;
import com.bondarenko.movieland.service.security.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TokenBlacklist — a service for managing a blacklist of JWT tokens.
 * <p>
 * This class allows adding tokens to a temporary in-memory blacklist
 * (e.g., after user logout) and automatically cleans up expired tokens.
 */

@CacheService
@AllArgsConstructor
public class TokenBlacklist {
    private TokenService tokenService;
    private final Map<String, Long> blacklist = new ConcurrentHashMap<>();

    public void addToken(String token) {
        long expiryTimeMillis = tokenService.getExpirationMillis(token);
        blacklist.putIfAbsent(token, expiryTimeMillis);
    }


    @Scheduled(fixedDelayString = "${movieland.security.token.expiry-ms}")
    private void cleanupExpiredTokens() {
        long now = System.currentTimeMillis();
        blacklist.entrySet().removeIf(token -> token.getValue() < now);
    }

    public boolean isBlacklisted(String token) {
        return blacklist.containsKey(token);
    }
}