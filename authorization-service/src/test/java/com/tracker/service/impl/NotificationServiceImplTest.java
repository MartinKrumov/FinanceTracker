package com.tracker.service.impl;

import com.tracker.common.util.mail.MailMessage;
import com.tracker.common.util.mail.MailType;
import com.tracker.config.IdpProperties;
import com.tracker.service.MailService;
import com.tracker.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Martin Krumov
 */
class NotificationServiceImplTest {

    private static final String RESET_CODE = UUID.randomUUID().toString();
    private static final String VERIFICATION_CODE = UUID.randomUUID().toString();

    private static final String EMAIL = "user@mail.com";

    @Mock
    private MailService mailService;
    @Mock
    private IdpProperties idpProperties;

    @Captor
    private ArgumentCaptor<MailMessage> messageCaptor;

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        initMocks(this);
        notificationService = new NotificationServiceImpl(mailService, idpProperties);
    }

    @Test
    void sendVerificationEmailSendsEmail() {
        //act
        notificationService.sendVerificationEmail(EMAIL, VERIFICATION_CODE);

        //assert
        verify(mailService).sendEmail(messageCaptor.capture());

        MailMessage message = messageCaptor.getValue();
        assertEquals(EMAIL, message.getTo());
        assertEquals(MailType.CONFIRM_EMAIL, message.getMailType());
    }
}
