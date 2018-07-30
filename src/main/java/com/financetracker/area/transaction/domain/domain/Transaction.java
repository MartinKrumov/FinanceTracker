package com.financetracker.area.transaction.domain.domain;

import com.financetracker.area.budget.domain.Budget;
import com.financetracker.area.category.domain.Category;
import com.financetracker.area.transaction.domain.enums.TransactionType;
import com.financetracker.area.wallet.domain.Wallet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name="wallet_id")
    private Wallet wallet;

    @ManyToOne
    @JoinColumn(name="budget_id")
    private Budget budget;
}
