package com.financetracker.area.category.models;

import com.financetracker.area.budget.domain.Budget;
import com.financetracker.area.transaction.domain.Transaction;
import com.financetracker.area.transaction.enums.TransactionType;
import com.financetracker.area.user.domain.User;
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
