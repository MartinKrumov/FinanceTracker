package com.tracker.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;
import java.util.Arrays;

import static java.util.stream.Collectors.joining;

/**
 * Default implementation of {@link AsyncUncaughtExceptionHandler}
 */
@Slf4j
public class DefaultAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... params) {
        String parameters = Arrays.stream(params)
                .map(Object::toString)
                .collect(joining(","));
        log.error("Unexpected exception occurred invoking async method: {} with parameters [{}] " , method, parameters, throwable);
    }
}
