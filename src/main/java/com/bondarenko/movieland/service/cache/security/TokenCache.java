package com.bondarenko.movieland.service.cache.security;

import com.bondarenko.movieland.service.annotation.CacheService;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@CacheService
public class TokenCache {
    @Value("${movieland.security.token.expiry-hours}")
    private long tokenExpiryHours;
    private final Map<UUID, Instant> tokenStore = new ConcurrentHashMap<>();

    public void putToken(UUID token) {
        Instant expirationTime = Instant.now().plus(tokenExpiryHours, ChronoUnit.HOURS);
        tokenStore.put(token, expirationTime);
    }

    public void removeToken(UUID token) {
        tokenStore.remove(token);
    }
}