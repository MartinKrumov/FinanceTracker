package com.financetracker.area.wallet.domain;

import com.financetracker.area.budget.domain.Budget;
import com.financetracker.area.transaction.domain.domain.Transaction;
import com.financetracker.area.user.domain.User;
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
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    
    @OneToMany(mappedBy="wallet")
    private List<Transaction> transactions;

    @OneToMany(mappedBy="wallet")
    private List<Budget> budgets;

}
