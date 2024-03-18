package com.tracker.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Data
@Entity
@Table(name = "budgets")
public class Budget implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "initial_amount", nullable = false)
    private BigDecimal initialAmount;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "from_date", nullable = false)
    private LocalDateTime fromDate;

    @Column(name = "to_date", nullable = false)
    private LocalDateTime toDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    public void addTransaction(@NonNull Transaction transaction) {
        if (isNull(transactions)) {
            transactions = new ArrayList<>();
        }

        transaction.setBudget(this);
        this.transactions.add(transaction);
    }

    public void removeTransaction(@NonNull Transaction transaction) {
        transaction.setBudget(null);
        this.transactions.remove(transaction);
    }
}
