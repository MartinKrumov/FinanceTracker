package com.tracker.dto.category;

import com.tracker.domain.Budget;
import com.tracker.domain.Transaction;
import com.tracker.domain.enums.TransactionType;
import com.tracker.domain.User;
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
