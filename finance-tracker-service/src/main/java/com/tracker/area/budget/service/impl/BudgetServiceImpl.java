package com.tracker.area.budget.service.impl;

import com.tracker.area.budget.domain.Budget;
import com.tracker.area.budget.model.BudgetRequestModel;
import com.tracker.area.budget.repository.BudgetRepository;
import com.tracker.area.budget.service.BudgetService;
import com.tracker.area.category.service.CategoryService;
import com.tracker.area.transaction.domain.Transaction;
import com.tracker.area.transaction.enums.TransactionType;
import com.tracker.area.user.services.UserService;
import com.tracker.area.wallet.repository.WalletRepository;
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

/**
 * @author Martin Krumov
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BudgetServiceImpl implements BudgetService {

    private final UserService userService;
    private final WalletRepository walletRepository;
    private final BudgetRepository budgetRepository;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void createBudget(BudgetRequestModel budgetRequestModel, Long userId, Long walletId) {
        var user = userService.findOneOrThrow(userId);
        var category = categoryService.findOneOrThrow(budgetRequestModel.getCategoryId());

        var wallet = user.getWallets().stream()
                .filter(w -> Objects.equals(w.getId(), walletId))
                .findFirst()
                .orElseThrow();

        var budget = modelMapper.map(budgetRequestModel, Budget.class);
        budget.setInitialAmount(budgetRequestModel.getAmount());
        budget.setCategory(category);
        budget.setWalletId(walletId);

        this.adjustBudgetAmount(wallet.getTransactions(), budget);
        category.getBudgets().add(budget);
        wallet.addBudget(budget);

        budgetRepository.save(budget);
    }

    private void adjustBudgetAmount(Collection<Transaction> transactions, Budget budget) {
        List<Transaction> existingTransactions = transactions.stream()
                .filter(t -> isBetweenDates(t.getDate(), budget.getFromDate(), budget.getToDate()) && t.getCategory().equals(budget.getCategory()))
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
