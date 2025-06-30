package com.bondarenko.movieland.service.security;

import com.bondarenko.movieland.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class JwtTokenServiceImplTest {

    private JwtTokenServiceImpl tokenService;
    private final String secret = "MySuperSecretKeyForJwtTokenMySuperSecretKey!";
    private final long expiryMs = 1000 * 60 * 60;

    @BeforeEach
    void setUp() {
        tokenService = new JwtTokenServiceImpl(secret, expiryMs);
    }

    @Test
    void generateToken_ShouldContainCorrectClaims() {
        String email = "Usain.Bolt@example.com";
        String nickname = "Usain Bolt";
        Role role = Role.ADMIN;

        String token = tokenService.generateToken(email, nickname, role);
        assertNotNull(token);
    }
}
