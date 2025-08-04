package com.bondarenko.movieland.controller;


import com.bondarenko.movieland.api.model.UserJWTResponse;
import com.bondarenko.movieland.configuration.SecurityConfig;
import com.bondarenko.movieland.service.auth.AuthService;
import com.bondarenko.movieland.service.security.TokenService;
import com.bondarenko.movieland.web.controller.AuthController;
import com.bondarenko.movieland.web.exception.InvalidCredentialsException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = true)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private TokenService tokenService;

    @Test
    @WithMockUser(roles = "USER")
    void shouldLoginSuccessfully() throws Exception {
        var requestJson = """
                    {
                        "email": "ronald.reynolds66@example.com",
                        "password": "paco"
                    }
                """;

        var userResponse = getUserJWTResponse();

        when(authService.login(any())).thenReturn(userResponse);

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("test.jwt.token"))
                .andExpect(jsonPath("$.nickname").value("Рональд Рейнольдс"))
                .andExpect(header().string("Authorization", "Bearer " + "test.jwt.token"));

        verify(authService).login(any());
    }


    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnBadRequestWhenLoginFails() throws Exception {
        var requestJson = """
                {
                    "email": "wrong@example.com",
                    "password": "wrongpass"
                }
                """;

        when(authService.login(any()))
                .thenThrow(new InvalidCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Login failed"))
                .andExpect(jsonPath("$.detail").value("Wrong email or password. Please check your credentials and try again."))
                .andExpect(jsonPath("$.path").value("/api/v1/login"));

        verify(authService).login(any());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldLogoutSuccessfully() throws Exception {
        String validToken = "valid.jwt.token";
        String authHeader = "Bearer " + validToken;

        doNothing().when(authService).logout(validToken);

        mockMvc.perform(delete("/api/v1/logout")
                        .header("Authorization", authHeader))
                .andExpect(status().isNoContent());

        verify(authService).logout(validToken);
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnBadRequestWhenAuthorizationHeaderMissing() throws Exception {
        mockMvc.perform(delete("/api/v1/logout"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(tokenService);
        verifyNoInteractions(authService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnBadRequestWhenAuthorizationHeaderMalformed() throws Exception {
        String malformedHeader = "BadTokenWithoutBearerPrefix";

        mockMvc.perform(delete("/api/v1/logout")
                        .header("Authorization", malformedHeader))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(tokenService);
        verifyNoInteractions(authService);
    }

    private @NotNull UserJWTResponse getUserJWTResponse() {
        var userResponse = new UserJWTResponse();
        userResponse.setToken("test.jwt.token");
        userResponse.setNickname("Рональд Рейнольдс");
        return userResponse;
    }
}
