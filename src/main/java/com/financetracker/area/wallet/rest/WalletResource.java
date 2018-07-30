package com.financetracker.area.wallet.rest;

import com.financetracker.area.wallet.models.WalletBindingModel;
import com.financetracker.area.wallet.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WalletResource {

    private final WalletService walletService;

    @PostMapping("/{userId}/wallet")
    public ResponseEntity createWallet(@RequestBody WalletBindingModel walletBindingModel, @PathVariable Long userId) {
        walletService.createWallet(walletBindingModel, userId);

        return new ResponseEntity(HttpStatus.CREATED);
    }
}
