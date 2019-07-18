package com.tracker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "auth")
@EnableConfigurationProperties(AuthProperties.class)
public class AuthProperties {

    /**
     * Holds client configuration
     */
    private Client client;

    @Valid
    private Jwt jwt;

    /**
     * Holds JWT token related configuration.
     **/
    @Data
    public static class Jwt {

        @NotNull
        private Duration validity;

        @NotNull
        private Duration refreshValidity;
    }

    @Data
    public static class Client {

        private String clientId;
        private String clientSecret;
        private List<String> grantTypes;
        private List<String> scope;
    }
}
