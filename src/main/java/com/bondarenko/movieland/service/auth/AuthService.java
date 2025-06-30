package com.bondarenko.movieland.service.auth;

import com.bondarenko.movieland.api.model.UserJWTResponse;
import com.bondarenko.movieland.api.model.UserRequest;

import java.util.UUID;

public interface AuthService {
    UserJWTResponse login(UserRequest userRequest);

    void logout(String token);


}
