package com.tracker.common.exception;

public class NonUniqueResultException extends RuntimeException {

    public NonUniqueResultException(String message) {
        super(message);
    }
}
