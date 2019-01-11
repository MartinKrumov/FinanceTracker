package com.financetracker.area.budget.domain;

import com.financetracker.area.category.domain.Category;
import com.financetracker.area.transaction.domain.Transaction;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "budgets")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal initialAmount;

    private BigDecimal amount;

    private LocalDateTime fromDate;

    private LocalDateTime toDate;

    @Column(name = "wallet_id")
    private Long walletId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "budget_id")
    private List<Transaction> transactions;
}
