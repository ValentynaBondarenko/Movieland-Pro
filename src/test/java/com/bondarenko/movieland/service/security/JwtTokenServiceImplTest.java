package com.bondarenko.movieland.service.security;

import com.bondarenko.movieland.entity.Role;
import com.bondarenko.movieland.entity.dto.UserDetails;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtTokenServiceImplTest {
    private JwtTokenServiceImpl tokenService;
    private final String secretBase64 = Base64.getEncoder().encodeToString("MySuperSecretKeyForJwtTokenMySuperSecretKey!".getBytes());
    private final long expiryMs = 1000 * 60 * 60;

    @BeforeEach
    void setUp() {
        tokenService = new JwtTokenServiceImpl();
        ReflectionTestUtils.setField(tokenService, "secretKey", secretBase64);
        ReflectionTestUtils.setField(tokenService, "tokenExpiryMs", expiryMs);
        tokenService.init();  // invoke @PostConstruct
    }

    @Test
    void generateToken_ShouldBeNotNull() {
        UserDetails userDetails = getUserDetails();
        String token = tokenService.generateToken(userDetails);
        assertNotNull(token);
    }

    @Test
    void isTokenValid_ReturnsTrue_ForValidTokenAndUserDetails() {
        //prepare
        UserDetails userDetails = getUserDetails();

        String token = tokenService.generateToken(userDetails);

        // than
        assertTrue(tokenService.isTokenValid(token, userDetails));
    }

    private @NotNull UserDetails getUserDetails() {
        String email = "Usain.Bolt@example.com";
        String nickname = "Usain Bolt";
        Role role = Role.ADMIN;
        return new UserDetails(email, role, nickname);
    }

}
