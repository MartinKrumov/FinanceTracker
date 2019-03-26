package com.tracker.rest;

import com.tracker.domain.Transaction;
import com.tracker.dto.transaction.TransactionRequest;
import com.tracker.mapper.TransactionMapper;
import com.tracker.service.CategoryService;
import com.tracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TransactionResource {

    private final CategoryService categoryService;
    private final TransactionMapper transactionMapper;
    private final TransactionService transactionService;

    @PostMapping("wallets/{walletId}/transactions")
    public ResponseEntity createTransaction(@Valid @RequestBody TransactionRequest transactionRequest,
                                            @PathVariable Long walletId) {
        log.info("Request for creating transaction has been received with walletId = [{}]", walletId);

        Transaction transaction = transactionMapper.convertToTransaction(transactionRequest);
        transaction.setCategory(categoryService.findByIdOrThrow(transactionRequest.getCategoryId()));

        transactionService.save(walletId, transaction);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("wallets/{walletId}/transactions/{transactionId}")
    public ResponseEntity deleteTransaction(@PathVariable Long walletId, @PathVariable Long transactionId) {
        log.info("Request for deleting transaction has been received with walletId = [{}] and transactionId = [{}]",
                walletId, transactionId);

        transactionService.delete(walletId, transactionId);
        return ResponseEntity.noContent().build();
    }
}
