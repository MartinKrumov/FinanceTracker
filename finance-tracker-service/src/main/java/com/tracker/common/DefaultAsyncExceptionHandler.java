package com.tracker.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * Default implementation of {@link AsyncUncaughtExceptionHandler}
 */
@Slf4j
public class DefaultAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... params) {
        log.error("Unexpected exception occurred invoking async method: " + method, throwable);

        for (Object param : params) {
            log.error("Parameter value: {}", param);
        }
    }
}
