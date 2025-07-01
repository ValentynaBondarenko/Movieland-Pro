package com.bondarenko.movieland.entity.dto;

import com.bondarenko.movieland.entity.Role;

public record UserDetails(String email, Role role, String nickname) {
}
