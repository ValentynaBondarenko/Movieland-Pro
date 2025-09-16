package com.bondarenko.movieland.service.cache.security;

import com.bondarenko.movieland.service.annotation.CacheService;
import com.bondarenko.movieland.service.security.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * TokenBlacklist — service for managing a blacklist of JWT tokens.
 *
 * <p>Tokens are stored in Redis with a TTL based on their expiration. Blacklisted tokens
 * (e.g., after logout) are automatically removed when expired, ensuring they cannot be used.
 */

@CacheService
@AllArgsConstructor
public class TokenBlacklist {
    private TokenService tokenService;
    private final StringRedisTemplate redis;

    public void addToken(String token) {
        try {
            long expiryTimeMillis = tokenService.getExpirationMillis(token);
            long ttlMillis = expiryTimeMillis - System.currentTimeMillis();

            if (ttlMillis > 0) {
                redis.opsForValue().set(token, "blacklisted", ttlMillis, TimeUnit.MILLISECONDS);
            } else {
                redis.opsForValue().set(token, "blacklisted", 60, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            // fallback
            redis.opsForValue().set(token, "blacklisted", 60, TimeUnit.SECONDS);
        }
    }

    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redis.hasKey(token));
    }
}