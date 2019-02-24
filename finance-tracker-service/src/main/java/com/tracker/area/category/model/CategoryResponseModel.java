package com.tracker.area.category.model;

import com.tracker.area.budget.domain.Budget;
import com.tracker.area.transaction.domain.Transaction;
import com.tracker.area.transaction.enums.TransactionType;
import com.tracker.area.user.domain.User;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class CategoryResponseModel {

    private String name;

    private TransactionType type;

    private Set<User> users;

    private List<Budget> budgets;

    private Set<Transaction> transactions;
}
