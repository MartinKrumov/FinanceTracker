package com.tracker.config;

import com.tracker.domain.enums.TokenType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.Map;

@Validated
@ConfigurationProperties(prefix = "idp")
public record IdpProperties(
        @NotBlank String ftUI,
        @NotNull @Positive Integer previousPasswordsLimit,
        @NotNull @Positive Integer loginAttemptsLimit,
        @NotNull Map<TokenType, @NotNull Duration> tokenTypeToValidity,
        @Valid AsyncProperties asyncProperties,
        @Valid MailProperties mailProperties) {

    public record AsyncProperties(
            @NotNull @Positive Integer corePoolSize,
            @NotNull @Positive Integer maxPoolSize,
            @NotNull @Positive Integer queueCapacity) {
    }

    public record MailProperties(
            @NotBlank String from,
            @NotBlank String email) {
    }
}
