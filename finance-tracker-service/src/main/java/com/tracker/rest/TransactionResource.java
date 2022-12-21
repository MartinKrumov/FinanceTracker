package com.tracker.rest;

import com.tracker.domain.Transaction;
import com.tracker.mapper.TransactionMapper;
import com.tracker.rest.dto.transaction.TransactionCreationDTO;
import com.tracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallets")
public class TransactionResource {

    private final TransactionMapper transactionMapper;
    private final TransactionService transactionService;

    @PostMapping("/{walletId}/transactions")
    public ResponseEntity<Void> createTransaction(@Valid @RequestBody TransactionCreationDTO transactionCreationDTO,
                                            @PathVariable Long walletId) {
        log.info("Request for creating transaction has been received with walletId = [{}]", walletId);

        Transaction transaction = transactionMapper.toTransaction(transactionCreationDTO);

        transactionService.save(walletId, transaction, transactionCreationDTO.getCategoryId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{walletId}/transactions/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long walletId, @PathVariable Long transactionId) {
        log.info("Request for deleting transaction has been received with walletId = [{}] and transactionId = [{}]",
                walletId, transactionId);

        transactionService.delete(walletId, transactionId);
        return ResponseEntity.noContent().build();
    }
}
