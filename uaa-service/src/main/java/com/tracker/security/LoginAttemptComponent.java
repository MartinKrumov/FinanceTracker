package com.tracker.security;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;
import com.tracker.config.IdpProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class LoginAttemptComponent {

    private static final int DEFAULT_VALUE = 1;

    private final IdpProperties idpProperties;

    private final IMap<ClientIpToUsername, Integer> usernameToLoginAttempts;

    @Autowired
    public LoginAttemptComponent(IdpProperties idpProperties) {
        this.idpProperties = idpProperties;
        this.usernameToLoginAttempts =
                Hazelcast.getHazelcastInstanceByName("auth-service").getMap("usernameToLoginAttempts");
    }

    void loginSucceeded(ClientIpToUsername clientIpToUsername) {
        usernameToLoginAttempts.remove(clientIpToUsername);
    }

    void loginFailed(ClientIpToUsername clientIpToUsername) {
        updateIfPresent(clientIpToUsername);
        usernameToLoginAttempts.putIfAbsent(clientIpToUsername, DEFAULT_VALUE, 30, TimeUnit.MINUTES);
    }

    boolean isBlocked(ClientIpToUsername clientIpToUsername) {
        return usernameToLoginAttempts.get(clientIpToUsername) >= idpProperties.getLoginAttemptsLimit();
    }

    /**
     * If a entry exist, update the value.
     *
     * @param clientIpToUsername the key
     */
    private void updateIfPresent(ClientIpToUsername clientIpToUsername) {
        if (usernameToLoginAttempts.containsKey(clientIpToUsername)) {
            usernameToLoginAttempts.put(clientIpToUsername, incrementValue(clientIpToUsername), 30, TimeUnit.MINUTES);
        }
    }

    /**
     * Increment the value for given key
     *
     * @param clientIpToUsername the key
     * @return incremented value
     */
    private int incrementValue(ClientIpToUsername clientIpToUsername) {
        return usernameToLoginAttempts.get(clientIpToUsername) + DEFAULT_VALUE;
    }
}
