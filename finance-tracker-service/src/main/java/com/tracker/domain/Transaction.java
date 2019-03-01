package com.tracker.domain;

import com.tracker.domain.enums.TransactionType;
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
    @JoinColumn(name="category_id")
    private Category category;

    @Column(name = "wallet_id")
    private Long walletId;

    @Column(name = "budget_id")
    private Long budgetId;
}
