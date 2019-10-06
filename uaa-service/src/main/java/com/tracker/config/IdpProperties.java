package com.tracker.config;

import com.tracker.domain.enums.TokenType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.Duration;
import java.util.Map;

@Data
@Validated
@ConfigurationProperties(prefix = "idp")
public class IdpProperties {

    /** The location of FT UI */
    @NotBlank
    private String ftUI;

    @NotNull
    @Positive
    private Integer previousPasswordsLimit;

    @NotNull
    @Positive
    private Integer loginAttemptsLimit;

    /** Holds token validity related configuration. */
    private Map<TokenType, @NotNull Duration> tokenTypeToValidity;

    /** Holds Async configuration properties. */
    @Valid
    private final AsyncProperties async;

    /** Holds default email information configuration. */
    @Valid
    private final MailProperties mail;

    public IdpProperties() {
        this.async = new AsyncProperties();
        this.mail = new MailProperties();
    }

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
