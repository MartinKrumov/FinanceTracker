package com.tracker.common.util.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MailType {

    CONFIRM_EMAIL("confirm-email", "Welcome to FinanceTracker! Confirm Your Email"),
    RESET_PASSWORD("reset-password", "Reset Your Password");

    private final String template;
    private final String subject;
}
