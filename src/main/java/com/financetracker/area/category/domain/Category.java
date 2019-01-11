package com.financetracker.area.category.domain;

import com.financetracker.area.budget.domain.Budget;
import com.financetracker.area.transaction.domain.Transaction;
import com.financetracker.area.transaction.enums.TransactionType;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated
    @Column(name = "type", nullable = false)
    private TransactionType type;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany(mappedBy = "category")
    private List<Budget> budgets;

    @OneToMany(mappedBy = "category")
    private List<Transaction> transactions;
}
