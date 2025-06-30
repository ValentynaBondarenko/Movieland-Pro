package com.bondarenko.movieland.service.auth;

import com.bondarenko.listener.DataSourceListener;
import com.bondarenko.movieland.api.model.UserRequest;
import com.bondarenko.movieland.api.model.UserJWTResponse;
import com.bondarenko.movieland.service.AbstractITest;
import com.bondarenko.movieland.web.exception.InvalidCredentialsException;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@DBRider
class UserServiceITest extends AbstractITest {
    @Autowired
    private AuthService userService;

    @BeforeEach
    void resetDataSourceListener() {
        DataSourceListener.reset();
    }

    @Test
    @DataSet(value = "datasets/user/dataset_users.yml")
    @ExpectedDataSet(value = "datasets/user/dataset_users.yml")
    void login_withValidCredentials_returnsUserResponse() {
        // given
        UserRequest userRequest = new UserRequest("ronald.reynolds66@example.com", "paco");

        // when
        UserJWTResponse userResponse = userService.login(userRequest);

        // then
        assertNotNull(userResponse);
        assertEquals("Рональд Рейнольдс", userResponse.getNickname());
        assertNotNull(userResponse.getToken(), "UUID token must be generated");
        DataSourceListener.assertSelectCount(1);
    }

    @Test
    @DataSet(value = "datasets/user/dataset_users.yml")
    void login_withInvalidPassword_throwsInvalidCredentialsException() {
        // given
        UserRequest userRequest = new UserRequest("ronald.reynolds66@example.com", "wrongPassword");

        // expect
        assertThrows(InvalidCredentialsException.class, () -> userService.login(userRequest));
    }

}
