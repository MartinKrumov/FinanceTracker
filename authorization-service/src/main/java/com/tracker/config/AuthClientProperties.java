package com.tracker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "auth")
@EnableConfigurationProperties(AuthClientProperties.class)
public class AuthClientProperties {

    private final Client client = new Client();

    @Data
    public static class Client {

        private String clientId;
        private String clientSecret;
        private List<String> grantTypes;
        private List<String> scope;
    }
}
