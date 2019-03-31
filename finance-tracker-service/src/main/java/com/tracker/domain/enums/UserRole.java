package com.tracker.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  UserRole {
    USER(0),
    ROLE(1);

    private final int value;
}
