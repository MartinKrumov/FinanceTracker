package com.financetracker.area.wallet.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletResource {

    @PostMapping("/{userId}/create_wallet")
    public ResponseEntity createWallet(@PathVariable Long userId) {

        return ResponseEntity.ok(200);
    }
}
