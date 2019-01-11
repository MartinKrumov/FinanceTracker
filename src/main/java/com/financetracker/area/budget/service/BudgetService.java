package com.financetracker.area.budget.service;

import com.financetracker.area.budget.model.BudgetRequestModel;

/**
 * @author Martin Krumov
 */
public interface BudgetService {

    void createBudget(BudgetRequestModel budgetRequestModel, Long userId, Long walletId);
}
