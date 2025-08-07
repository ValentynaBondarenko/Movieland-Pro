package com.bondarenko.movieland.web.filter;

import com.bondarenko.movieland.service.security.TokenService;
import com.bondarenko.movieland.service.user.UserService;
import com.bondarenko.movieland.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Catch Http-request->Read JWT -> check signature (exp)-> Ok->
 * Spring create new Authentication object
 * and add to Security Context  ( SecurityContextHolder.getContext().setAuthentication(authToken))
 */
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserService userService;

    public JwtAuthorizationFilter(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //JWT
        final String token = JWTUtil.extractAccessToken(request);

        try {

            if (tokenService.isRefreshToken(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            String email = tokenService.extractEmailFromToken(token); // from request
            UserDetails userDetails = userService.loadUserByUsername(email);//from DB

            if (tokenService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid or expired token\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}

