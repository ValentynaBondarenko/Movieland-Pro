package com.bondarenko.movieland.service.user;

import com.bondarenko.movieland.api.model.UserRequest;
import com.bondarenko.movieland.api.model.UserUUIDResponse;

import java.util.UUID;

public interface UserService {
    UserUUIDResponse login(UserRequest userRequest);

    void logout(UUID uuid);


}
