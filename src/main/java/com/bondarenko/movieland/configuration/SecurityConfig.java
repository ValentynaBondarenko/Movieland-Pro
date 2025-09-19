package com.bondarenko.movieland.configuration;

import com.bondarenko.movieland.service.cache.security.TokenBlacklist;
import com.bondarenko.movieland.service.security.TokenService;
import com.bondarenko.movieland.service.user.UserService;
import com.bondarenko.movieland.web.filter.JwtAuthenticationFilter;
import com.bondarenko.movieland.web.filter.JwtAuthorizationFilter;
import com.bondarenko.movieland.web.filter.JwtLogoutFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration; 
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity //for methods @PreAuthorize("hasRole('ADMIN')") @WithMockUser(roles = "USER")
public class SecurityConfig {

    private final TokenService tokenService;
    private final UserService userService;
    private final TokenBlacklist tokenBlacklist;

    public SecurityConfig(TokenService tokenService, UserService userService, TokenBlacklist tokenBlacklist) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.tokenBlacklist = tokenBlacklist;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, tokenService);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/v1/login");

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/movies").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/movies/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/login", "/api/v1/logout", "/api/v1/refresh").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                        )
                )
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtLogoutFilter(tokenBlacklist), JwtAuthorizationFilter.class)
                .build();
    }

    //custom filter for authorization with JWT and real User
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(tokenService, userService, tokenBlacklist);
    }

    //use UserDetailsService +PasswordEncoder
    //works with my custom JwtAuthenticationFilter
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtLogoutFilter jwtLogoutFilter(TokenBlacklist tokenBlacklist) {
        return new JwtLogoutFilter(tokenBlacklist);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
