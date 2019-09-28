package com.tracker.service;

import com.tracker.common.util.mail.MailMessage;
import org.springframework.scheduling.annotation.Async;

public interface MailService {

    /**
     * Send email with mail message. Works asynchronously using {@link Async}
     *
     * @param mailMessage the {@link MailMessage}
     */
    void sendEmail(MailMessage mailMessage);
}
