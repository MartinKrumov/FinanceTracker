package com.tracker.service.impl;

import com.tracker.common.util.FinanceUtils;
import com.tracker.domain.*;
import com.tracker.domain.enums.TransactionType;
import com.tracker.dto.budget.BudgetRequestModel;
import com.tracker.service.BudgetService;
import com.tracker.service.CategoryService;
import com.tracker.service.UserService;
import com.tracker.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BudgetServiceImpl implements BudgetService {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final WalletService walletService;
    private final CategoryService categoryService;

    @Override
    @Transactional
    public void createBudget(BudgetRequestModel budgetRequestModel, Long userId, Long walletId) {
        User user = userService.findByIdOrThrow(userId);
        Category category = categoryService.findOneOrThrow(budgetRequestModel.getCategoryId());

        Wallet wallet = user.getWallets().stream()
                .filter(w -> Objects.equals(w.getId(), walletId))
                .findFirst()
                .orElseThrow();

        Budget budget = modelMapper.map(budgetRequestModel, Budget.class);
        budget.setCategory(category);

        this.adjustBudgetAmount(budget, wallet.getTransactions());
        budget.setInitialAmount(budgetRequestModel.getAmount());
        wallet.addBudget(budget);

        walletService.save(wallet);
    }

    @Override
    public void adjustBudgetAmount(Budget budget, Transaction transaction) {
        if (TransactionType.EXPENSE.equals(transaction.getType())) {
            budget.setAmount(budget.getAmount().subtract(transaction.getAmount()));
        } else {
            budget.setAmount(budget.getAmount().add(transaction.getAmount()));
        }
    }

    private void adjustBudgetAmount(Budget budget, Collection<Transaction> transactions) {
        List<Transaction> existingTransactions = transactions.stream()
                .filter(transaction -> FinanceUtils.isTransactionInBudgetRange(budget, transaction))
                .collect(toList());

        BigDecimal totalAmount = FinanceUtils.calculateAdjustedAmount(existingTransactions);

        budget.setAmount(budget.getAmount().add(totalAmount));
    }
}
