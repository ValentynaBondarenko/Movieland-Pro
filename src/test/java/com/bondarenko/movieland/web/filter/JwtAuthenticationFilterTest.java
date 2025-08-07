package com.bondarenko.movieland.web.filter;

import com.bondarenko.movieland.service.security.TokenService;
import com.bondarenko.movieland.service.security.dto.JwtTokens;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    private AuthenticationManager authenticationManager;
    private TokenService tokenService;
    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        authenticationManager = mock(AuthenticationManager.class);
        tokenService = mock(TokenService.class);
        filter = new JwtAuthenticationFilter(authenticationManager, tokenService);
    }

    @Test
    void attemptAuthentication_shouldCallAuthenticateWithParsedUserRequest() throws Exception {
        //prepare
        String json = "{\"email\":\"test@example.com\",\"password\":\"pass123\"}";
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getInputStream()).thenReturn(
                new jakarta.servlet.ServletInputStream() {
                    private final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

                    @Override
                    public int read() {
                        return byteArrayInputStream.read();
                    }

                    @Override
                    public boolean isFinished() {
                        return byteArrayInputStream.available() == 0;
                    }

                    @Override
                    public boolean isReady() {
                        return true;
                    }

                    @Override
                    public void setReadListener(jakarta.servlet.ReadListener readListener) {
                    }
                }
        );

        Authentication expectedAuth = mock(Authentication.class);

        ArgumentCaptor<UsernamePasswordAuthenticationToken> captor = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        when(authenticationManager.authenticate(captor.capture())).thenReturn(expectedAuth);

        Authentication result = filter.attemptAuthentication(request, response);

        assertEquals(expectedAuth, result);
        UsernamePasswordAuthenticationToken token = captor.getValue();
        assertEquals("test@example.com", token.getPrincipal());
        assertEquals("pass123", token.getCredentials());
    }

    @Test
    void successfulAuthentication_shouldWriteTokensToResponse() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        UserDetails userDetails = mock(UserDetails.class);
        Authentication authResult = new UsernamePasswordAuthenticationToken(userDetails, null, null);

        JwtTokens jwtTokens = new JwtTokens("access-token", "refresh-token");
        when(tokenService.generateToken(userDetails)).thenReturn(jwtTokens);

        var stringWriter = new java.io.StringWriter();
        var printWriter = new java.io.PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        filter.successfulAuthentication(request, response, chain, authResult);

        printWriter.flush();
        String responseBody = stringWriter.toString();

        assertTrue(responseBody.contains("access-token"));
        assertTrue(responseBody.contains("refresh-token"));
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
    }
}