package com.bondarenko.movieland.util;

import java.util.UUID;

public final class TokenUtil {
    private TokenUtil() {
    }

    public static UUID generateUUID() {
        return UUID.randomUUID();
    }
}
