package com.bondarenko.movieland.service.auth;

import com.bondarenko.listener.DataSourceListener;
import com.bondarenko.movieland.api.model.UserJWTResponse;
import com.bondarenko.movieland.api.model.UserRequest;
import com.bondarenko.movieland.entity.Role;
import com.bondarenko.movieland.service.auth.dto.UserDetails;
import com.bondarenko.movieland.repository.UserRepository;
import com.bondarenko.movieland.service.AbstractITest;
import com.bondarenko.movieland.service.cache.security.TokenBlacklist;
import com.bondarenko.movieland.service.security.TokenService;
import com.bondarenko.movieland.web.exception.InvalidCredentialsException;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@DBRider
class AuthServiceImplITest extends AbstractITest {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private TokenBlacklist tokenBlacklist;

    @BeforeEach
    void setUp() {
        DataSourceListener.reset();
    }

    @Test
    @DataSet(value = "datasets/auth/user_dataset.yml", cleanBefore = true)
    void testLoginWithValidCredentials() {
        // given
        UserRequest loginRequest = new UserRequest("ronald.reynolds66@example.com", "qwerty");
        UserDetails expectedDetails = new UserDetails(
                "ronald.reynolds66@example.com",
                Role.USER,
                "Рональд Рейнольдс"
        );
        // when
        UserJWTResponse response = authService.login(loginRequest);

        // then
        assertNotNull(response);
        assertEquals("Рональд Рейнольдс", response.getNickname());
        assertNotNull(response.getToken());
        assertTrue(tokenService.isTokenValid(response.getToken(), expectedDetails));

        DataSourceListener.assertSelectCount(1);
    }

    @Test
    @DataSet(value = "datasets/auth/user_dataset.yml", cleanBefore = true)
    void testLoginWithInvalidPasswordThrowsException() {
        UserRequest request = new UserRequest("ronald.reynolds66@example.com", "wrong_password");

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        assertTrue(exception.getMessage().contains("Invalid password "));
    }

    @Test
    @DataSet(value = "datasets/auth/user_dataset.yml", cleanBefore = true)
    void testLogoutAddsTokenToBlacklist() {
        // given
        UserRequest request = new UserRequest("ronald.reynolds66@example.com", "qwerty");

        // when
        UserJWTResponse response = authService.login(request);
        String token = response.getToken();
        // ensure token is generated
        assertNotNull(token);

        assertFalse(tokenBlacklist.isBlacklisted(token), "Token should not be blacklisted before logout");

        // now logout
        authService.logout(token);

        // then
        assertTrue(tokenBlacklist.isBlacklisted(token), "Token should be blacklisted after logout");
    }

}
