package com.bondarenko.movieland.service.security;

import com.bondarenko.movieland.entity.Role;

public interface TokenService {
    String generateToken(String email, String nickname, Role role);

}
