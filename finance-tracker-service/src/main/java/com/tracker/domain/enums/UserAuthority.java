package com.tracker.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserAuthority {
    USER(0),
    ADMIN(1);

    private final int value;
}
