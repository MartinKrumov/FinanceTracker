package com.tracker.area.transaction.service;

import com.tracker.area.transaction.domain.Transaction;

import java.util.List;

/**
 * @author Martin Krumov
 */
public interface TransactionService {
    List<Transaction> findAllByWalletId(Long walletId);
}
