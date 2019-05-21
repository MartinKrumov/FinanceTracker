package com.tracker.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionType {
    INCOME(0),
    EXPENSE(1);

    private final int value;
}
