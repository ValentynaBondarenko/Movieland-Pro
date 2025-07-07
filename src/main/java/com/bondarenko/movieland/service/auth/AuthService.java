package com.bondarenko.movieland.service.auth;

import com.bondarenko.movieland.api.model.UserJWTResponse;
import com.bondarenko.movieland.api.model.UserRequest;

public interface AuthService {
    UserJWTResponse login(UserRequest userRequest);

    void logout(String token);

}
