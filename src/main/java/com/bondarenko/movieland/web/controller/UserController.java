package com.bondarenko.movieland.web.controller;

import com.bondarenko.movieland.api.LoginApi;
import com.bondarenko.movieland.api.LogoutApi;
import com.bondarenko.movieland.api.model.UserRequest;
import com.bondarenko.movieland.api.model.UserResponse;
import com.bondarenko.movieland.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController implements LoginApi, LogoutApi {
    private final UserService securityService;

    @PostMapping("/login")
    @Override
    public ResponseEntity<UserResponse> loginUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("Login user request: {}", userRequest.getEmail());

        UserResponse userResponse = securityService.login(userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);

    }

    @DeleteMapping("/logout")
    @Override
    public ResponseEntity<Void> logoutUser(@RequestHeader("uuid") UUID uuid) {
        log.info("Logout user with user id: {}", uuid);

        securityService.logout(uuid);
        return ResponseEntity.noContent().build();
    }

}
