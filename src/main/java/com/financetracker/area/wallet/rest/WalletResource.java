package com.financetracker.area.wallet.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financetracker.area.wallet.models.WalletBindingModel;
import com.financetracker.area.wallet.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WalletResource {

    private final WalletService walletService;

    @PostMapping("users/{userId}/wallets")
    public ResponseEntity createWallet(@Valid @RequestBody WalletBindingModel walletBindingModel, @PathVariable Long userId) {
        walletService.createWallet(walletBindingModel, userId);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping(value = "/date", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity parse(@RequestBody String json) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> map = objectMapper.readValue(json, new TypeReference<Map<String,Object>>(){});

        long timestamp = (Integer) map.get("timestamp");

        LocalDateTime triggerTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.of("UTC"));

        return ResponseEntity.ok(triggerTime);
    }
}
