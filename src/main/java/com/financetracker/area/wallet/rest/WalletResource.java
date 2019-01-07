package com.financetracker.area.wallet.rest;

import com.financetracker.area.wallet.models.WalletBindingModel;
import com.financetracker.area.wallet.models.WalletInfoResponseDTO;
import com.financetracker.area.wallet.models.WalletResponseModel;
import com.financetracker.area.wallet.services.WalletService;
import lombok.RequiredArgsConstructor;
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
        walletService.createWallet(walletBindingModel, userId);

        return new ResponseEntity(HttpStatus.CREATED);
    }


    @GetMapping("users/{userId}/wallets")
    public ResponseEntity<List<WalletResponseModel>> getWallet(@PathVariable Long userId) {
        List<WalletResponseModel> walletServiceAllByUserId = walletService.findAllByUserId(userId);
        return ResponseEntity.ok(walletServiceAllByUserId);
    }

    @GetMapping("users/{userId}/wallets/{walletId}")
    public ResponseEntity<WalletInfoResponseDTO> getWallet(@PathVariable Long userId, @PathVariable Long walletId) {
        log.debug("Request for walletId = {} from userId= {} ", walletId, userId);
        WalletInfoResponseDTO walletInfoDTO = walletService.findByIdAndUser(walletId, userId);
        return ResponseEntity.ok(walletInfoDTO);
    }
}
