package com.bondarenko.movieland.dto;

import com.bondarenko.movieland.api.model.UserResponse;

import java.time.Instant;

public record TokenInfo(
        UserResponse userResponse,
        Instant expiryTime
) {}
