package com.bondarenko.movieland.service.security;

import com.bondarenko.movieland.entity.dto.UserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Service
public class JwtTokenServiceImpl implements TokenService {
    @Value("${movieland.security.token.secret}")
    private String secretKey;
    @Value("${movieland.security.token.expiry-ms}")
    private long tokenExpiryMs;

    private SecretKey key;

    @PostConstruct
    void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.email())
                .claim("nickname", userDetails.nickname())
                .claim("role", userDetails.role().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiryMs))
                .signWith(key)
                .compact();
    }

//    @Override
//    public boolean isTokenValid(String token, UserDetails userDetails) {
//        String email = extractEmailFromToken(token);
//        String nickname = extractNicknameFromToken(token);
//        String role = extractRoleFromToken(token);
//
//        return nickname.equals(userDetails.nickname())
//                && role.equals(userDetails.role().name())
//                && email.equals(userDetails.email())
//                && !isTokenExpired(token);
//
//    }

    @Override
    public void validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            if (isTokenExpired(token)) {
                throw new JwtException("Token expired");
            }
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            throw e;
        }
    }

    // Expired  time
    @Override
    public Long getExpirationMillis(String token) {
        return extractExpiration(token).getTime();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //Claims
    private String extractNicknameFromToken(String token) {
        return extractClaim(token, claims -> claims.get("nickname", String.class));
    }

    private String extractRoleFromToken(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    private String extractEmailFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token).getPayload();

            return claimsResolver.apply(claims);

        } catch (SecurityException | MalformedJwtException e) {
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
        } catch (ExpiredJwtException e) {
            throw new AuthenticationCredentialsNotFoundException("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            throw new AuthenticationCredentialsNotFoundException("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            throw new AuthenticationCredentialsNotFoundException("JWT token compact of handler are invalid.");
        }
    }


}
