package com.financetracker.area.category.models;

import com.financetracker.area.transaction.enums.TransactionType;
import lombok.Data;

@Data
public class CategoryRequestModel {

    private String name;
    private TransactionType type;
    private Long userId;
}
