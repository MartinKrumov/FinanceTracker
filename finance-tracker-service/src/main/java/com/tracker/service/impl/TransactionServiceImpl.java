package com.tracker.service.impl;

import com.tracker.common.util.FinanceUtils;
import com.tracker.domain.Budget;
import com.tracker.domain.Transaction;
import com.tracker.domain.Wallet;
import com.tracker.service.BudgetService;
import com.tracker.service.TransactionService;
import com.tracker.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Martin Krumov
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionServiceImpl implements TransactionService {

    private final WalletService walletService;
    private final BudgetService budgetService;

    @Override
    @Transactional
    public void save(Long walletId, Transaction transaction) {
        Wallet wallet = walletService.findByIdOrThrow(walletId);

        Budget budget = wallet.getBudgets().stream()
                .filter(b -> FinanceUtils.isTransactionInBudgetRange(b, transaction))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Non unique result found"));

        budgetService.adjustBudgetAmount(budget, transaction);
        walletService.adjustWalletAmount(wallet, transaction);

        wallet.addTransaction(transaction);
        budget.addTransaction(transaction);
        walletService.save(wallet);
    }

}
