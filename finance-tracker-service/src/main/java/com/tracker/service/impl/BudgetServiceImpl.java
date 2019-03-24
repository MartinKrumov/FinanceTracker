package com.tracker.service.impl;

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
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BudgetServiceImpl implements BudgetService {

    private final WalletService walletService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

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

        this.adjustBudgetAmount(wallet.getTransactions(), budget);
        budget.setInitialAmount(budgetRequestModel.getAmount());
        wallet.addBudget(budget);

        walletService.save(wallet);
    }

    private void adjustBudgetAmount(Collection<Transaction> transactions, Budget budget) {
        List<Transaction> existingTransactions = transactions.stream()
                .filter(t -> isBetweenDates(t.getDate(), budget.getFromDate(), budget.getToDate()) &&
                        Objects.equals(t.getCategory(), budget.getCategory()))
                .collect(toList());

        if (isNotEmpty(existingTransactions)) {
            BigDecimal amount = existingTransactions.stream()
                    .filter(t -> TransactionType.EXPENSE.equals(t.getType()))
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            budget.setAmount(budget.getAmount().subtract(amount));
        }
    }

    private boolean isBetweenDates(LocalDateTime date, LocalDateTime fromDate, LocalDateTime toDate) {
        return date.isAfter(fromDate) && date.isBefore(toDate);
    }
}
