package com.tracker.area.wallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WalletNameAlreadyExists extends RuntimeException {

    public WalletNameAlreadyExists(String message) {
        super(message);
    }
}
