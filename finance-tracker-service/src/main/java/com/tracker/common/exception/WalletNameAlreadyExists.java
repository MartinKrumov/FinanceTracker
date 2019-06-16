package com.tracker.common.exception;

public class WalletNameAlreadyExists extends RuntimeException {

    public WalletNameAlreadyExists(String message) {
        super(message);
    }
}
