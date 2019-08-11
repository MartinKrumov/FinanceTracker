package com.tracker.config;

import com.tracker.domain.enums.TokenType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.Duration;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "idp")
public class IdpProperties {

    /** The location of FT UI */
    @NotBlank
    private String ftUI;

    @NotNull
    @Positive
    private Integer previousPasswordsLimit;

    /** Holds token validity related configuration. */
    private Map<TokenType, @NotNull Duration> tokenTypeToValidity;

    /** Holds Async configuration properties. */
    @Valid
    private AsyncProperties async;

    /** Holds default email information configuration. */
    @Valid
    private MailProperties mail;

    @Data
    static class AsyncProperties {

        @NotNull
        @Positive
        private Integer corePoolSize;

        @NotNull
        @Positive
        private Integer maxPoolSize;

        @NotNull
        @Positive
        private Integer queueCapacity;
    }

    @Data
    public static class MailProperties {

        @NotBlank
        private String from;

        @NotBlank
        private String email;
    }

}
