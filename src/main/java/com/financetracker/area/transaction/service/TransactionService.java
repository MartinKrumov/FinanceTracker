package com.financetracker.area.transaction.service;

import com.financetracker.area.transaction.domain.Transaction;

import java.util.List;

/**
 * @author Martin Krumov
 */
public interface TransactionService {
    List<Transaction> findAllByWalletId(Long walletId);
}
