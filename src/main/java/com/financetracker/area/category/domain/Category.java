package com.financetracker.area.category.domain;

import com.financetracker.area.budget.domain.Budget;
import com.financetracker.area.transaction.domain.domain.Transaction;
import com.financetracker.area.transaction.domain.enums.TransactionType;
import com.financetracker.area.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToMany(mappedBy = "categories")
    private Set<User> users;

    @OneToMany(mappedBy="category", fetch = FetchType.EAGER)
    private List<Budget> budgets;

    @OneToMany(mappedBy="category", fetch = FetchType.EAGER)
    private Set<Transaction> transactions;
}
