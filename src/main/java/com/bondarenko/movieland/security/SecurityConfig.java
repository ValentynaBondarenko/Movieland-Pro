package com.bondarenko.movieland.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((authorizeHttpRequests) ->
//                        authorizeHttpRequests
//                                .requestMatchers(HttpMethod.POST, "/movies/**").hasRole("ADMIN")
//                                .requestMatchers(HttpMethod.PUT, "/movies/**").hasRole("ADMIN")
//                )
//                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll);
//        return http.build();
//    }
}


