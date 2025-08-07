package com.bondarenko.movieland.web.filter;

import com.bondarenko.movieland.service.cache.security.TokenBlacklist;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class JwtLogoutFilterTest {

    private TokenBlacklist tokenBlacklist;
    private JwtLogoutFilter jwtLogoutFilter;

    @BeforeEach
    void setUp() {
        tokenBlacklist = mock(TokenBlacklist.class);
        jwtLogoutFilter = new JwtLogoutFilter(tokenBlacklist);
    }

    @Test
    void doFilterInternal_logoutPath_addsTokensToBlacklistAndReturnsOk() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getServletPath()).thenReturn("/api/v1/logout");
        when(request.getMethod()).thenReturn("POST");

        when(request.getHeader("Authorization")).thenReturn("Bearer access-token");
        when(request.getHeader("Refresh-Token")).thenReturn("refresh-token");

        jwtLogoutFilter.doFilterInternal(request, response, filterChain);

        verify(tokenBlacklist).addToken("access-token");
        verify(tokenBlacklist).addToken("refresh-token");

        verify(response).setStatus(HttpServletResponse.SC_OK);

        verifyNoInteractions(filterChain);
    }

    @Test
    void doFilterInternal_nonLogoutPath_callsFilterChain() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getServletPath()).thenReturn("/api/v1/other");
        when(request.getMethod()).thenReturn("GET");

        jwtLogoutFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        verifyNoInteractions(tokenBlacklist);

        verify(response, never()).setStatus(anyInt());
    }
}