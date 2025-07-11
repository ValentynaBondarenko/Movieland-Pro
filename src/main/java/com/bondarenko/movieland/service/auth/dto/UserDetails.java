package com.bondarenko.movieland.service.auth.dto;

import com.bondarenko.movieland.entity.Role;

public record UserDetails(String email, Role role, String nickname) {
}

