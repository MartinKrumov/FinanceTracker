package com.tracker.domain;

import com.tracker.domain.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "categories")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name",nullable = false)
    private String name;

    @Enumerated
    @Column(name = "type", nullable = false)
    private TransactionType type;
}
