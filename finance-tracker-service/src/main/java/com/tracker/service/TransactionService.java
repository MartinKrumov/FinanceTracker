package com.tracker.service;

import com.tracker.domain.Transaction;

import java.util.List;

/**
 * @author Martin Krumov
 */
public interface TransactionService {
    List<Transaction> findAllByWalletId(Long walletId);
}
