package com.bondarenko.movieland.web.filter;

import com.bondarenko.movieland.service.security.TokenService;
import com.bondarenko.movieland.service.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthorizationFilterTest {

    private TokenService tokenService;
    private UserService userService;
    private JwtAuthorizationFilter filter;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        tokenService = mock(TokenService.class);
        userService = mock(UserService.class);
        filter = new JwtAuthorizationFilter(tokenService, userService);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);

        // clean SecurityContext before each test
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_validToken_setsAuthenticationAndContinues() throws Exception {
        String token = "valid-token";
        String email = "user@example.com";

        UserDetails userDetails = mock(UserDetails.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        mockStaticJwtUtilExtractAccessToken(token);

        when(tokenService.isRefreshToken(token)).thenReturn(false);
        when(tokenService.extractEmailFromToken(token)).thenReturn(email);
        when(userService.loadUserByUsername(email)).thenReturn(userDetails);
        when(tokenService.isTokenValid(token, userDetails)).thenReturn(true);
        when(userDetails.getAuthorities()).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        SecurityContext ctx = SecurityContextHolder.getContext();
        assertNotNull(ctx.getAuthentication());
        assertInstanceOf(UsernamePasswordAuthenticationToken.class, ctx.getAuthentication());
        assertEquals(userDetails, ctx.getAuthentication().getPrincipal());

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_refreshToken_callsFilterChainWithoutAuth() throws Exception {
        String token = "refresh-token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        mockStaticJwtUtilExtractAccessToken(token);

        when(tokenService.isRefreshToken(token)).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        // SecurityContext must be empty
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_invalidToken_returnsUnauthorized() throws Exception {
        String token = "bad-token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        mockStaticJwtUtilExtractAccessToken(token);

        when(tokenService.isRefreshToken(token)).thenReturn(false);
        when(tokenService.extractEmailFromToken(token)).thenThrow(new RuntimeException("Invalid token"));

        StringWriter responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        assertTrue(responseWriter.toString().contains("Invalid or expired token"));

        verify(filterChain, never()).doFilter(request, response);
    }

    private void mockStaticJwtUtilExtractAccessToken(String token) {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
    }
}