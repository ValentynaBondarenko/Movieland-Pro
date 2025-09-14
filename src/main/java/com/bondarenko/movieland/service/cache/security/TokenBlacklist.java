package com.bondarenko.movieland.service.cache.security;

import com.bondarenko.movieland.service.annotation.CacheService;
import com.bondarenko.movieland.service.security.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

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
    private final StringRedisTemplate redis;

    public void addToken(String token) {
        long expiryTimeMillis = tokenService.getExpirationMillis(token);
        long ttlMillis = expiryTimeMillis - System.currentTimeMillis();

        if (ttlMillis > 0) {
            redis.opsForValue().set(token, "blacklisted", ttlMillis, TimeUnit.MILLISECONDS);
        }
    }

    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redis.hasKey(token));
    }
}