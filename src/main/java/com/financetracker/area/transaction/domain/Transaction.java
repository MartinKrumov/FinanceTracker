package com.financetracker.area.transaction.domain;

import com.financetracker.area.budget.domain.Budget;
import com.financetracker.area.category.domain.Category;
import com.financetracker.area.transaction.enums.TransactionType;
import com.financetracker.area.wallet.domain.Wallet;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated
    @Column(name = "type", nullable = false)
    private TransactionType type;

    private BigDecimal amount;

    private LocalDateTime date;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="wallet_id")
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="budget_id")
    private Budget budget;
}
