package com.tracker.service;

import com.tracker.domain.Budget;

public interface BudgetService {

    void createBudget(Budget budget, Long userId, Long walletId);
}
