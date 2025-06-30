package com.bondarenko.movieland.web.controller;

import com.bondarenko.movieland.api.LoginApi;
import com.bondarenko.movieland.api.LogoutApi;
import com.bondarenko.movieland.api.model.UserJWTResponse;
import com.bondarenko.movieland.api.model.UserRequest;
import com.bondarenko.movieland.service.auth.AuthService;
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
        log.info("Login user request: {}", userRequest.getEmail());

        UserJWTResponse userResponse = securityService.login(userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);

    }

    @DeleteMapping("/logout")
    @Override
    public ResponseEntity<Void> logoutUser(@RequestHeader("token") String token) {
        log.info("Logout user with user token: {}", token);

        securityService.logout(token);
        return ResponseEntity.noContent().build();
    }

}
