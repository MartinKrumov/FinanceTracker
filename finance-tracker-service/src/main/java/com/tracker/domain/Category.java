package com.tracker.domain;

import com.tracker.domain.enums.TransactionType;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "categories")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated
    @Column(name = "type", nullable = false)
    private TransactionType type;

    @OneToMany(mappedBy = "category")
    private List<Budget> budgets;

//    @OneToMany(mappedBy = "category")
//    private List<Transaction> transactions;
}
