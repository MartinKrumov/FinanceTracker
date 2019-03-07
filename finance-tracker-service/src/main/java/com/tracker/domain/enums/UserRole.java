package com.tracker.domain.enums;

import lombok.Getter;

@Getter
public enum  UserRole {
    USER(0),
    ROLE(1);

    private final int value;

    UserRole(int value) {
        this.value = value;
    }
}
