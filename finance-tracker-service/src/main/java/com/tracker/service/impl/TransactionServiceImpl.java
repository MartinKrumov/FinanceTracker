package com.tracker.service.impl;

import com.tracker.common.util.FinanceUtils;
import com.tracker.domain.Transaction;
import com.tracker.domain.Wallet;
import com.tracker.domain.enums.TransactionType;
import com.tracker.service.TransactionService;
import com.tracker.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final WalletService walletService;

    @Override
    @Transactional
    public void save(Long walletId, Transaction transaction) {
        Wallet wallet = walletService.findByIdOrThrow(walletId);

        wallet.getBudgets().stream()
                .filter(b -> FinanceUtils.isTransactionInBudgetRange(b, transaction))
                .findFirst()
                .ifPresent(budget -> {
                    budget.setAmount(calculateBalanceAfterCreate(budget.getAmount(), transaction));
                    budget.addTransaction(transaction);
                });

        wallet.setAmount(calculateBalanceAfterCreate(wallet.getAmount(), transaction));
        wallet.addTransaction(transaction);

        walletService.save(wallet);
    }

    @Override
    @Transactional
    public void delete(Long walletId, Long transactionId) {
        Wallet wallet = walletService.findByIdOrThrow(walletId);

        Transaction transaction = wallet.getTransactions().stream()
                .filter(t -> t.getId().equals(transactionId))
                .findFirst()
                .orElseThrow();

        wallet.getBudgets().stream()
                .filter(b -> FinanceUtils.isTransactionInBudgetRange(b, transaction))
                .findFirst()
                .ifPresent(budget -> {
                    budget.setAmount(calculateBalanceAfterRemove(budget.getAmount(), transaction));
                    budget.removeTransaction(transaction);
                });

        wallet.setAmount(calculateBalanceAfterRemove(wallet.getAmount(), transaction));
        wallet.removeTransaction(transaction);

        walletService.save(wallet);
    }

    @Override
    public BigDecimal calculateAdjustedAmount(BigDecimal amount, Collection<Transaction> transactions) {
        if (CollectionUtils.isEmpty(transactions)) {
            return amount;
        }

        BigDecimal balance = amount;
        for (Transaction t : transactions) {
            if (TransactionType.EXPENSE.equals(t.getType())) {
                balance = balance.subtract(t.getAmount());
            } else {
                balance = balance.add(t.getAmount());
            }
        }

        return balance;
    }

    /**
     * If the transaction type  is {@link TransactionType#EXPENSE} it subtracts from the amount
     * else it adds
     *
     * @param balance given balance
     * @param transaction {@link Transaction}
     * @return {@link BigDecimal} result
     */
    private BigDecimal calculateBalanceAfterCreate(BigDecimal balance, Transaction transaction) {
        boolean isExpense = TransactionType.EXPENSE.equals(transaction.getType());
        return isExpense ? balance.subtract(transaction.getAmount()) : balance.add(transaction.getAmount());
    }

    /**
     * If the transaction type  is {@link TransactionType#EXPENSE} it adds from the amount
     * else it subtracts
     *
     * @param balance given balance
     * @param transaction {@link Transaction}
     * @return {@link BigDecimal} result
     */
    private BigDecimal calculateBalanceAfterRemove(BigDecimal balance, Transaction transaction) {
        boolean isExpense = TransactionType.EXPENSE.equals(transaction.getType());
        return isExpense ? balance.add(transaction.getAmount()) : balance.subtract(transaction.getAmount());
    }
}
