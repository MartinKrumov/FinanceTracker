package com.tracker.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "finance-tracker")
public record FinanceTrackerProperties(
        @NotBlank String corsOrigins,
        @Valid AsyncProperties asyncProperties,
        @Valid JwtProperties jwtProperties) {

    public record JwtProperties(
            @NotBlank String authoritiesKey,
            @NotBlank String jwtSecret) {
    }

    public record AsyncProperties(
            @NotNull @Positive Integer corePoolSize,
            @NotNull @Positive Integer maxPoolSize,
            @NotNull @Positive Integer queueCapacity) {
    }
}
