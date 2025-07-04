package com.bondarenko.movieland.controller;


import com.bondarenko.movieland.api.model.UserUUIDResponse;
import com.bondarenko.movieland.configuration.SecurityConfig;
import com.bondarenko.movieland.service.user.UserService;
import com.bondarenko.movieland.web.controller.UserController;
import com.bondarenko.movieland.web.exception.InvalidCredentialsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = true)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "USER")
    void shouldLoginSuccessfully() throws Exception {
        var requestJson = """
                    {
                        "email": "ronald.reynolds66@example.com",
                        "password": "paco"
                    }
                """;

        var userResponse = new UserUUIDResponse();
        userResponse.setUuid(UUID.fromString("e5e84a87-2732-422e-8b1a-bd61ad7ec399"));
        userResponse.setNickname("Рональд Рейнольдс");

        when(userService.login(any())).thenReturn(userResponse);

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value("e5e84a87-2732-422e-8b1a-bd61ad7ec399"))
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
        String uuid = "e5e84a87-2732-422e-8b1a-bd61ad7ec399";

        doNothing().when(userService).logout(UUID.fromString(uuid));

        mockMvc.perform(delete("/api/v1/logout")
                        .header("uuid", uuid))
                .andExpect(status().isNoContent());

        verify(userService).logout(UUID.fromString(uuid));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnBadRequestOnInvalidUuidLogout() throws Exception {
        String invalidUuid = "not-a-uuid";

        mockMvc.perform(delete("/api/v1/logout")
                        .header("uuid", invalidUuid))
                .andExpect(status().isBadRequest());

        verify(userService, never()).logout(any());
    }
}
