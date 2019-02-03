package com.tracker.area.category.models;

import com.tracker.area.transaction.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryInfoDTO implements Serializable {

    private Long id;
    private String name;
    private TransactionType type;
}