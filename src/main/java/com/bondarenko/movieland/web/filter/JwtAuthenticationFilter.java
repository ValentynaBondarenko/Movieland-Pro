package com.bondarenko.movieland.web.filter;

import com.bondarenko.movieland.api.model.UserRequest;
import com.bondarenko.movieland.service.security.TokenService;
import com.bondarenko.movieland.service.security.dto.JwtTokens;
import com.bondarenko.movieland.service.security.dto.UserJWTResponse;
import com.bondarenko.movieland.web.exception.LoginParsingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        setFilterProcessesUrl("/api/v1/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            UserRequest authRequest = new ObjectMapper().readValue(request.getInputStream(), UserRequest.class);

            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());
            return authenticationManager.authenticate(token);

        } catch (IOException e) {
            throw new LoginParsingException("Failed to parse login request");
        }
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                         Authentication authResult) throws IOException {


        JwtTokens jwt = tokenService.generateToken((UserDetails) authResult.getPrincipal());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        UserJWTResponse body = new UserJWTResponse(
                jwt.accessToken(),
                jwt.refreshToken()
        );
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    }

}
