package com.bondarenko.movieland.service.auth;

import com.bondarenko.movieland.api.dto.UserJWTResponse;
import com.bondarenko.movieland.api.dto.UserRequest;

public interface AuthService {
    UserJWTResponse login(UserRequest userRequest);

    void logout(String token);


}
