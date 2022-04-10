package com.tracker.config;

import com.tracker.common.DefaultAsyncExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    private final IdpProperties.AsyncProperties asyncProperties;

    public AsyncConfig(IdpProperties idpProperties) {
        this.asyncProperties = idpProperties.getAsync();
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(asyncProperties.getCorePoolSize());
        executor.setMaxPoolSize(asyncProperties.getMaxPoolSize());
        executor.setQueueCapacity(asyncProperties.getQueueCapacity());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("async-");
        executor.initialize();

        return new DelegatingSecurityContextAsyncTaskExecutor(executor);
    }

    @Bean
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new DefaultAsyncExceptionHandler();
    }
}
