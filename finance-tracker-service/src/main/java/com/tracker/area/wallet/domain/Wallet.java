package com.tracker.area.wallet.domain;

import com.tracker.area.budget.domain.Budget;
import com.tracker.area.transaction.domain.Transaction;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private BigDecimal initialAmount;

    @Column(name = "user_id")
    private Long userId;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "wallet_id")
    private List<Transaction> transactions;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "wallet_id")
    private List<Budget> budgets;

    public void addBudget(Budget budget) {
        this.budgets.add(budget);
    }
    public void removeBudget(Budget budget) {
        this.budgets.remove(budget);
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }
    public void removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
    }
}