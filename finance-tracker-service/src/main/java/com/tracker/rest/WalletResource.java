package com.tracker.rest;

import com.tracker.domain.Wallet;
import com.tracker.rest.dto.wallet.CreateWalletDTO;
import com.tracker.rest.dto.wallet.WalletDetailsDTO;
import com.tracker.rest.dto.wallet.WalletInfoDTO;
import com.tracker.mapper.WalletMapper;
import com.tracker.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WalletResource {

    private final WalletMapper walletMapper;
    private final WalletService walletService;

    @PostMapping("users/{userId}/wallets")
    public ResponseEntity createWallet(@Valid @RequestBody CreateWalletDTO createWalletDTO, @PathVariable Long userId) {
        log.info("Request for creating wallet has been received with userId = [{}]", userId);

        Wallet wallet = walletMapper.convertToWallet(createWalletDTO);

        walletService.createWallet(wallet, userId);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("users/{userId}/wallets")
    public ResponseEntity<Collection<WalletDetailsDTO>> getWallet(@PathVariable Long userId) {
        log.info("Request for getting wallets for user with userId = [{}] has been received.", userId);

        Set<Wallet> wallets = walletService.findAllByUserId(userId);

        return ResponseEntity.ok(walletMapper.convertToCategoryResponseModels(wallets));
    }

    @GetMapping("users/{userId}/wallets/{walletId}")
    public ResponseEntity<WalletInfoDTO> getWalletInfo(@PathVariable Long userId, @PathVariable Long walletId) {
        log.info("Request for getting wallet with walletId = [{}] from userId = [{}] .", walletId, userId);
        WalletInfoDTO walletInfoDTO = walletService.findByIdAndUser(userId, walletId);
        return ResponseEntity.ok(walletInfoDTO);
    }
}
