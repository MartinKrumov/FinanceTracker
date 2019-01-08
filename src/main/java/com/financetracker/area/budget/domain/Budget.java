package com.financetracker.area.budget.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.financetracker.area.category.domain.Category;
import com.financetracker.area.transaction.domain.Transaction;
import com.financetracker.area.wallet.domain.Wallet;
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

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    @OneToMany(mappedBy = "wallet")
    private List<Transaction> transactions;
}
