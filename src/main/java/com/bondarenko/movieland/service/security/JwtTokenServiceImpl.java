package com.bondarenko.movieland.service.security;

import com.bondarenko.movieland.service.security.dto.AuthenticatedUser;
import com.bondarenko.movieland.service.security.dto.JwtTokens;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
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
    public JwtTokens generateToken(UserDetails userDetails) {
        AuthenticatedUser user = (AuthenticatedUser) userDetails;

        Date now = new Date();
        Date accessExp = new Date(now.getTime() + tokenExpiryMs);
        Date refreshExp = new Date(now.getTime() + tokenExpiryMs * 10);

        String accessToken = Jwts.builder()
                .subject(user.email())
                .claim("nickname", user.nickname())
                .claim("role", user.role().name())
                .issuedAt(now)
                .expiration(accessExp)
                .signWith(key)
                .compact();

        String refreshToken = Jwts.builder()
                .subject(user.email())
                .claim("type", "refresh")
                .issuedAt(now)
                .expiration(refreshExp)
                .signWith(key)
                .compact();
        return new JwtTokens(accessToken, refreshToken);
    }

    //JwtTokens
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        AuthenticatedUser user = (AuthenticatedUser) userDetails;

        String email = extractEmailFromToken(token);
        String nickname = extractNicknameFromToken(token);
        String role = extractRoleFromToken(token);

        return nickname.equals(user.nickname())
                && role.equals(user.role().name())
                && email.equals(user.email())
                && !isTokenExpired(token);
    }

    // Expired  time
    @Override
    public Long getExpirationMillis(String token) {
        return extractExpiration(token).getTime();
    }

    @Override
    public boolean isRefreshToken(String token) {
        return "refresh".equals(
                extractClaim(token, claims -> claims.get("type", String.class))
        );
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

    @Override
    public String extractEmailFromToken(String token) {
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
