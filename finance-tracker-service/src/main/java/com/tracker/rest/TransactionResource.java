package com.tracker.rest;

import com.tracker.domain.Transaction;
import com.tracker.dto.transaction.TransactionRequest;
import com.tracker.mapper.TransactionMapper;
import com.tracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TransactionResource {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @PostMapping("wallets/{walletId}/transactions")
    public ResponseEntity createTransaction(@Valid @RequestBody TransactionRequest transactionRequest,
                                            @PathVariable Long walletId) {
        log.info("Request for creating transaction has been received with walletId = [{}]", walletId);

        Transaction transaction = transactionMapper.convertToTransaction(transactionRequest);

        transactionService.save(walletId, transaction);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
