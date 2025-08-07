package com.bondarenko.movieland.web.filter;

import com.bondarenko.movieland.service.cache.security.TokenBlacklist;
import com.bondarenko.movieland.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtLogoutFilter extends OncePerRequestFilter {
    private final TokenBlacklist tokenBlacklist;
    private static final String LOGOUT_URL = "/api/v1/logout";

    public JwtLogoutFilter(TokenBlacklist tokenBlacklist) {
        this.tokenBlacklist = tokenBlacklist;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (LOGOUT_URL.equals(request.getServletPath()) && "POST".equalsIgnoreCase(request.getMethod())) {
            String accessToken = JWTUtil.extractAccessToken(request);
            String refreshToken = JWTUtil.extractRefreshToken(request);

            if (accessToken != null) {
                tokenBlacklist.addToken(accessToken);
            }
            if (refreshToken != null) {
                tokenBlacklist.addToken(refreshToken);
            }

            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
