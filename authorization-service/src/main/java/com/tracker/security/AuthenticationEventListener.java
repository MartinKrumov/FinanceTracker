package com.tracker.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class AuthenticationEventListener {

    private static final int MAX_ATTEMPTS = 2;// TODO: externalize
    private static final Map<String, Integer> usernameToLoginAttempts = new ConcurrentHashMap<>();

    @Autowired
    private HttpServletRequest request;//TODO move to util

    @EventListener({InteractiveAuthenticationSuccessEvent.class, AuthenticationSuccessEvent.class})
    public void onAuthenticationSuccess(AbstractAuthenticationEvent event) {
        String username = event.getAuthentication().getName();
        log.info(username);
        usernameToLoginAttempts.remove(username);
    }

    @EventListener
    public void onAuthenticationFailed(AuthenticationFailureBadCredentialsEvent event) {
        String username = event.getAuthentication().getName();
        log.info(username);

        usernameToLoginAttempts.computeIfPresent(username, (key, attempts) -> attempts + 1);
        usernameToLoginAttempts.putIfAbsent(username, 1);

        if (usernameToLoginAttempts.get(username) > MAX_ATTEMPTS) {
            //TODO: lock the account
        }
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

}
