package com.tracker.service;

import com.tracker.dto.budget.BudgetRequestModel;

/**
 * @author Martin Krumov
 */
public interface BudgetService {

    void createBudget(BudgetRequestModel budgetRequestModel, Long userId, Long walletId);
}
