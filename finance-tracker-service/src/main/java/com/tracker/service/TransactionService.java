package com.tracker.service;

import com.tracker.domain.Transaction;
import com.tracker.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * @author Martin Krumov
 */
public interface TransactionService {

    void save(Long walletId, Transaction transaction, Long categoryId);

    void delete(Long walletId, Long transactionId);

    /**
     * Calculates the total amount of given transactions.
     * Sums all {@link TransactionType#INCOME} and from them subtract all {@link TransactionType#EXPENSE}
     *
     * @param amount given amount
     * @param transactions {@link Collection} of {@link Transaction}s
     * @return {@link BigDecimal} total amount
     */
    BigDecimal calculateAdjustedAmount(BigDecimal amount, Collection<Transaction> transactions);
}
