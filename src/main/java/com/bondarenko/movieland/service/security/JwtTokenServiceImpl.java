package com.bondarenko.movieland.service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtTokenServiceImpl implements TokenService {
    private final Key key;
    private final long tokenExpiryMs;
   // private final JwtParser jwtParser;

    public JwtTokenServiceImpl(
            @Value("${movieland.security.token.secret}") String secret,
            @Value("${movieland.security.token.expiry-ms}") long tokenExpiryMs
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));//alg HS256, HS384,
        this.tokenExpiryMs = tokenExpiryMs;
    }

    @Override
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiryMs))
                .signWith(key)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        //jwtParser.parseClaimsJws()

        return false;
    }

    @Override
    public String extractExpireDate(String token) {
        return "";
    }


}
