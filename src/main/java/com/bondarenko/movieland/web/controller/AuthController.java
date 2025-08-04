package com.bondarenko.movieland.web.controller;

import com.bondarenko.movieland.api.LoginApi;
import com.bondarenko.movieland.api.LogoutApi;
import com.bondarenko.movieland.api.model.UserJWTResponse;
import com.bondarenko.movieland.api.model.UserRequest;
import com.bondarenko.movieland.service.auth.AuthService;
import com.bondarenko.movieland.service.security.TokenService;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController implements LoginApi, LogoutApi {
    private final AuthService securityService;

    @PostMapping("/login")
    @Override
    public ResponseEntity<UserJWTResponse> loginUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("Login user request with email {} ", userRequest.getEmail());

        UserJWTResponse userResponse = securityService.login(userRequest);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + userResponse.getToken())
                .body(userResponse);
    }

    @DeleteMapping("/logout")
    @Override
    public ResponseEntity<Void> logoutUser(@RequestHeader("Authorization") String authHeader) {
        log.info("Logout request: {}", authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);
        try {
            //bad code
            //controller -> dispatcher and handler of business logic
            securityService.logout(token);
            return ResponseEntity.noContent().build();
        } catch (JwtException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
