package com.tracker.service.impl;

import com.tracker.common.util.FinanceUtils;
import com.tracker.domain.Budget;
import com.tracker.domain.Transaction;
import com.tracker.domain.User;
import com.tracker.domain.Wallet;
import com.tracker.service.BudgetService;
import com.tracker.service.TransactionService;
import com.tracker.service.UserService;
import com.tracker.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final UserService userService;
    private final WalletService walletService;
    private final TransactionService transactionService;

    @Override
    @Transactional
    public void createBudget(Budget budget, Long userId, Long walletId) {
        User user = userService.findByIdOrThrow(userId);

        Wallet wallet = user.getWallets().stream()
                .filter(w -> Objects.equals(w.getId(), walletId))
                .findFirst()
                .orElseThrow();

        this.adjustBudgetAmount(budget, wallet.getTransactions());
        budget.setInitialAmount(budget.getAmount());
        wallet.addBudget(budget);

        walletService.save(wallet);
    }

    private void adjustBudgetAmount(Budget budget, Collection<Transaction> transactions) {
        List<Transaction> existingTransactions = transactions.stream()
                .filter(transaction -> FinanceUtils.isTransactionInBudgetRange(budget, transaction))
                .collect(toList());

        BigDecimal totalAmount =
                transactionService.calculateAdjustedAmount(budget.getAmount(), existingTransactions);

        budget.setAmount(budget.getAmount().add(totalAmount));
    }
}
