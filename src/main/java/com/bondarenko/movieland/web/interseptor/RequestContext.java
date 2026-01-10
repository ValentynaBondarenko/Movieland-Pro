package com.bondarenko.movieland.web.interseptor;

public record RequestContext(String requestId, String user) {
    public static final ScopedValue<RequestContext> CURRENT_USER_INFO = ScopedValue.newInstance();
}
