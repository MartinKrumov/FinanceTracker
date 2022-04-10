package com.tracker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Configuration
@ConfigurationProperties("finance-tracker")
public class FinanceTrackerProperties {

    @NotBlank
    private String corsOrigins;

    @Valid
    private AsyncProperties async;

    @Valid
    private JwtProperties jwtProperties;

    @Data
    public static class JwtProperties {

        @NotBlank
        private String authoritiesKey;

        @NotBlank
        private String jwtSecret;
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

}
