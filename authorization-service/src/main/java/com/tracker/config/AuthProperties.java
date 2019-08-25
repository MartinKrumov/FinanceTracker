package com.tracker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;

@Data
@Validated
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {

    private final Client client;

    @Valid
    private final Jwt jwt;

    public AuthProperties() {
        this.client = new Client();
        this.jwt = new Jwt();
    }

    /** Holds JWT token related configuration. */
    @Data
    public static class Jwt {

        @NotNull
        private Duration validity;

        @NotNull
        private Duration refreshValidity;
    }

    /** Holds client configuration */
    @Data
    public static class Client {

        private String clientId;
        private String clientSecret;
        private List<String> grantTypes;
        private List<String> scope;
    }
}
