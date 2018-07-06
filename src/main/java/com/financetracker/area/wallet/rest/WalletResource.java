package com.financetracker.area.wallet.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletResource {

    @PostMapping("/create_wallet")
    public ResponseEntity createWallet() {

        return ResponseEntity.ok(200);
    }
}
