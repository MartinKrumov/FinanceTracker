package com.tracker.domain;

import com.tracker.domain.enums.TransactionType;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    private BigDecimal amount;

    private LocalDateTime date;

    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "wallet_id", insertable=false, updatable= false)
    private Long walletId;

    @Column(name = "budget_id")
    private Long budgetId;
}
