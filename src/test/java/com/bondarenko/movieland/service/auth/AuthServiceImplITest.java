package com.bondarenko.movieland.service.auth;

import com.bondarenko.listener.DataSourceListener;
import com.bondarenko.movieland.api.model.UserJWTResponse;
import com.bondarenko.movieland.api.model.UserRequest;
import com.bondarenko.movieland.repository.UserRepository;
import com.bondarenko.movieland.service.AbstractITest;
import com.bondarenko.movieland.web.exception.InvalidCredentialsException;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceImplITest extends AbstractITest {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        DataSourceListener.reset();
    }


    @Test
    @DataSet(value = "datasets/user/dataset_before_login.yml", cleanBefore = true)
    @ExpectedDataSet(value = "datasets/user/dataset_before_login.yml")
    void loginTest() {
        UserRequest request = new UserRequest("ronald.reynolds66@example.com", "paco");
        UserJWTResponse response = authService.login(request);

        assertNotNull(response.getToken());
        assertEquals("Рональд Рейнольдс", response.getNickname());
    }

    @Test
    void login_withValidUser_returnsJwtResponse() {
        UserRequest request = new UserRequest("ronald.reynolds66@example.com", "paco");
        UserJWTResponse response = authService.login(request);

        assertNotNull(response.getToken());
        assertEquals("Рональд Рейнольдс", response.getNickname());
    }

    @Test
    void login_withInvalidPassword_throwsException() {
        UserRequest request = new UserRequest("ronald.reynolds66@example.com", "wrongpass");

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
    }
}
