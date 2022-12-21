package com.tracker.security;

import com.tracker.common.util.HttpRequestUtils;
import com.tracker.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationEventListener {

    private final HttpServletRequest request;
    private final LoginAttemptComponent loginAttemptComponent;
    private final UserService userService;

    public AuthenticationEventListener(HttpServletRequest request, LoginAttemptComponent loginAttemptComponent, UserService userService) {
        this.request = request;
        this.loginAttemptComponent = loginAttemptComponent;
        this.userService = userService;
    }

    @EventListener({InteractiveAuthenticationSuccessEvent.class, AuthenticationSuccessEvent.class})
    public void onAuthenticationSuccess(AbstractAuthenticationEvent event) {
        ClientIpToUsername clientIpToUsername = buildIpAddressUsername(event.getAuthentication());
        loginAttemptComponent.loginSucceeded(clientIpToUsername);
    }

    @EventListener
    public void onAuthenticationFailed(AuthenticationFailureBadCredentialsEvent event) {
        ClientIpToUsername clientIpToUsername = buildIpAddressUsername(event.getAuthentication());
        loginAttemptComponent.loginFailed(clientIpToUsername);

        if (loginAttemptComponent.isBlocked(clientIpToUsername)) {
            userService.lockByUsername(clientIpToUsername.getUsername());
        }
    }

    /**
     * Creates {@link ClientIpToUsername} from the username and the IP address
     *
     * @param authentication the current user
     * @return {@link ClientIpToUsername}
     */
    private ClientIpToUsername buildIpAddressUsername(Authentication authentication) {
        String username = authentication.getName();
        String ipAddress = HttpRequestUtils.getClientIpAddress(request);
        log.debug("Username: {}, with IP: {}", username, ipAddress);
        return new ClientIpToUsername(ipAddress, username);
    }
}
