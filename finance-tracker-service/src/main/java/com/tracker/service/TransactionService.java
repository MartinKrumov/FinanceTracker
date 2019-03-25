package com.tracker.service;

import com.tracker.domain.Transaction;

/**
 * @author Martin Krumov
 */
public interface TransactionService {

    void save(Long walletId, Transaction transaction);
}
