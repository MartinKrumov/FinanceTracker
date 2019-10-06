package com.tracker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Configuration
@ConfigurationProperties("finance-tracker")
public class FinanceTrackerProperties {

    @Valid
    private AsyncProperties async;

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
