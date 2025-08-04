package com.bondarenko.movieland.service.security;

import com.bondarenko.movieland.service.auth.dto.UserDetails;

public interface TokenService {
    String generateToken(UserDetails userDetails);

    Long getExpirationMillis(String token);



    boolean isTokenValid(String token, UserDetails userDetails);
}
