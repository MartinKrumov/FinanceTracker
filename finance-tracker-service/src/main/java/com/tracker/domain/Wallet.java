package com.tracker.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Data
@Entity
@ToString(exclude = {"transactions"})
@Table(name = "wallets")
public class Wallet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "initial_amount", nullable = false)
    private BigDecimal initialAmount;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "wallet_id", nullable = false)
    private List<Budget> budgets;

    public void addBudget(@NonNull Budget budget) {
        if (isNull(budgets)) {
            budgets = new ArrayList<>();
        }
        budgets.add(budget);
    }

    public void addTransaction(@NonNull Transaction transaction) {
        if (isNull(transactions)) {
            transactions = new ArrayList<>();
        }

        transaction.setWallet(this);
        this.transactions.add(transaction);
    }

    public void removeTransaction(@NonNull Transaction transaction) {
        transaction.setWallet(null);
        this.transactions.remove(transaction);
    }
}
