package com.tracker.area.budget.service;

import com.tracker.area.budget.model.BudgetRequestModel;

/**
 * @author Martin Krumov
 */
public interface BudgetService {

    void createBudget(BudgetRequestModel budgetRequestModel, Long userId, Long walletId);
}
