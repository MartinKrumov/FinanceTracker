package com.tracker.service.impl;

import com.tracker.common.util.mail.MailMessage;
import com.tracker.common.util.mail.MailType;
import com.tracker.config.IdpProperties;
import com.tracker.service.MailService;
import com.tracker.service.NotificationService;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
public class NotificationServiceImpl implements NotificationService {

    //TODO: point to UI
    private static final String VERIFY_EMAIL_URL = "%s/api/users/complete-register?token=%s";
    private static final String RESET_PASSWORD_URL = "%s/api/users/validate-token?token=%s";

    private final MailService mailService;
    private final IdpProperties idpProperties;

    public NotificationServiceImpl(MailService mailService, IdpProperties idpProperties) {
        this.mailService = mailService;
        this.idpProperties = idpProperties;
    }

    @Override
    public void sendVerificationEmail(String email, String verificationToken) {
        Context context = new Context();
        context.setVariable("verificationURL",
                String.format(VERIFY_EMAIL_URL, idpProperties.getFtUI(), verificationToken));

        mailService.sendEmail(buildMailMessage(email, context, MailType.CONFIRM_EMAIL));
    }

    @Override
    public void sendPasswordResetEmail(String email, String resetToken) {
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("resetPasswordURL",
                String.format(RESET_PASSWORD_URL, idpProperties.getFtUI(), resetToken));

        mailService.sendEmail(buildMailMessage(email, context, MailType.RESET_PASSWORD));
    }

    private MailMessage buildMailMessage(String email, Context context, MailType mailType) {
        return MailMessage.builder()
                .to(email)
                .context(context)
                .mailType(mailType)
                .build();
    }
}
