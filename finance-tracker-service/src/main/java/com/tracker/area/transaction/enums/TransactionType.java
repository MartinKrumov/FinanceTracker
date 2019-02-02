package com.tracker.area.transaction.enums;

public enum TransactionType {
    INCOME(0),EXPENSE(1);

    private final int value;

    TransactionType(int value) {
        this.value = value;
    }
}
