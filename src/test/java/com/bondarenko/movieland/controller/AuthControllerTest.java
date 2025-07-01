package com.bondarenko.movieland.controller;


import com.bondarenko.movieland.api.dto.UserJWTResponse;
import com.bondarenko.movieland.configuration.SecurityConfig;
import com.bondarenko.movieland.service.auth.AuthService;
import com.bondarenko.movieland.service.security.TokenService;
import com.bondarenko.movieland.web.controller.AuthController;
import com.bondarenko.movieland.web.exception.InvalidCredentialsException;
import io.jsonwebtoken.JwtException;
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
    private AuthService userService;

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

        var userResponse = new UserJWTResponse();
        //todo Valid token
        userResponse.setToken("e5e84a87-2732-422e-8b1a-bd61ad7ec399");
        userResponse.setNickname("Рональд Рейнольдс");

        when(userService.login(any())).thenReturn(userResponse);

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("e5e84a87-2732-422e-8b1a-bd61ad7ec399"))
                .andExpect(jsonPath("$.nickname").value("Рональд Рейнольдс"));

        verify(userService).login(any());
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

        when(userService.login(any())).thenThrow(new InvalidCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Login failed"))
                .andExpect(jsonPath("$.detail").value("Wrong email or password. Please check your credentials and try again."))
                .andExpect(jsonPath("$.path").value("/api/v1/login"));

        verify(userService).login(any());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldLogoutSuccessfully() throws Exception {
        String token = "e5e84a87-2732-422e-8b1a-bd61ad7ec399";

        doNothing().when(userService).logout(token);
//todo Valid token
        mockMvc.perform(delete("/api/v1/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        verify(userService).logout(token);
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnBadRequestOnInvalidTokenLogout() throws Exception {
        String invalidJwt = "not-a-token";
        doThrow(new JwtException("Invalid token"))
                .when(tokenService).validateToken(invalidJwt);

        mockMvc.perform(delete("/api/v1/logout")
                        .header("Authorization", "Bearer " + invalidJwt))
                .andExpect(status().isBadRequest());

        verify(userService, never()).logout(any());
    }
}
