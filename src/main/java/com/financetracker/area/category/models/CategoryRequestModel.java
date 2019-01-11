package com.financetracker.area.category.models;

import com.financetracker.area.transaction.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestModel {

    private String name;
    private TransactionType type;
}
