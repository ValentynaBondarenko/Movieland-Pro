package com.bondarenko.movieland.service.cache.security;

import com.bondarenko.movieland.service.annotation.CacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@CacheService
public class TokenBlacklist {
    private final Map<String, Long> blacklist = new ConcurrentHashMap<>();

    public void addToken(String token) {

        //  blacklist.putIfAbsent(token, expiryTimeMillis);
    }

    @Scheduled(fixedDelayString = "${movieland.security.token.expiry-ms}")
    public void cleanupExpiredTokens() {
        long now = System.currentTimeMillis();
        blacklist.entrySet().removeIf(token -> token.getValue() < now);
    }

}