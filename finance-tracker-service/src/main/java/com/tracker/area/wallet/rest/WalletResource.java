package com.tracker.area.wallet.rest;

import com.tracker.area.wallet.model.WalletBindingModel;
import com.tracker.area.wallet.model.WalletInfoResponseDTO;
import com.tracker.area.wallet.model.WalletResponseModel;
import com.tracker.area.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WalletResource {

    private final WalletService walletService;

    @PostMapping("users/{userId}/wallets")
    public ResponseEntity createWallet(@Valid @RequestBody WalletBindingModel walletBindingModel, @PathVariable Long userId) {
        log.info("Request for creating wallet has been received with userId = [{}]", userId);
        walletService.createWallet(walletBindingModel, userId);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("users/{userId}/wallets")
    public ResponseEntity<List<WalletResponseModel>> getWallet(@PathVariable Long userId) {
        log.info("Request for getting wallets for user with userId = [{}] has been received.", userId);
        List<WalletResponseModel> walletServiceAllByUserId = walletService.findAllByUserId(userId);
        return ResponseEntity.ok(walletServiceAllByUserId);
    }

    @GetMapping("users/{userId}/wallets/{walletId}")
    public ResponseEntity<WalletInfoResponseDTO> getWallet(@PathVariable Long userId, @PathVariable Long walletId) {
        log.info("Request for getting wallet with walletId = [{}] from userId = [{}] .", walletId, userId);
        WalletInfoResponseDTO walletInfoDTO = walletService.findByIdAndUser(userId, walletId);
        return ResponseEntity.ok(walletInfoDTO);
    }
}
