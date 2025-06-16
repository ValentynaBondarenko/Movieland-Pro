package com.bondarenko.movieland.service.user;

import com.bondarenko.movieland.api.model.UserRequest;
import com.bondarenko.movieland.api.model.UserResponse;

import java.util.UUID;

public interface UserService {
    UserResponse login(UserRequest userRequest);

    void logout(UUID uuid);


}
