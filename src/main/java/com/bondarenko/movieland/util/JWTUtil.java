package com.bondarenko.movieland.util;

import jakarta.servlet.http.HttpServletRequest;

public class JWTUtil {

    public static String extractAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    public static String extractRefreshToken(HttpServletRequest request) {
        return request.getHeader("Refresh-Token");
    }
}
