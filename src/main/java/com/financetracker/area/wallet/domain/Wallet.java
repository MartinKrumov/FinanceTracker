package com.financetracker.area.wallet.domain;

import com.financetracker.area.budget.domain.Budget;
import com.financetracker.area.transaction.domain.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long walletId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private long userId;

    @Column(nullable = false)
    private BigDecimal initialAmount;
    
    @OneToMany(mappedBy="transactions", fetch = FetchType.EAGER)
    private List<Transaction> transactions;

    @OneToMany(mappedBy="budgets", fetch = FetchType.EAGER)
    private List<Budget> budgets;

}
