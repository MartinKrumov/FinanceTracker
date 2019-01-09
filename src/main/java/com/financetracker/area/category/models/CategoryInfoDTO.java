package com.financetracker.area.category.models;

import com.financetracker.area.transaction.enums.TransactionType;
import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryInfoDTO implements Serializable {

    private Long id;
    private String name;
    private TransactionType type;
}