package com.bondarenko.movieland.web.interseptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String REQUEST_ID = "requestId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String requestId = request.getHeader(REQUEST_ID);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "guest";

        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof String string) {
                username = string;
            } else if (principal instanceof UserDetails userDetails) {
                username = userDetails.getUsername();
            }
        }
        RequestContext ctx = new RequestContext(requestId, username);
        log.info("RequestId={}, User={}", ctx.requestId(), ctx.user());

        ScopedValue.runWhere(RequestContext.CURRENT_USER_INFO, ctx, () -> {
        });

        return true;
    }

}