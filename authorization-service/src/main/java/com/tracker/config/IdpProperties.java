package com.tracker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.Duration;

@Data
@Configuration
@ConfigurationProperties(prefix = "idp")
public class IdpProperties {

    @NotNull
    @Positive
    private Integer previousPasswordsLimit;

    @Valid
    private Token token;

    /** Holds Async configuration properties  */
    @Valid
    private AsyncProperties async;

    /** Holds token validity related configuration. */
    @Data
    public static class Token {

        @NotNull
        private Duration reset;

        @NotNull
        private Duration verification;
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
