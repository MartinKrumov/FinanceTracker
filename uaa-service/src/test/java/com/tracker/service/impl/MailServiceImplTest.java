package com.tracker.service.impl;

import com.tracker.common.util.mail.MailMessage;
import com.tracker.common.util.mail.MailType;
import com.tracker.config.IdpProperties;
import com.tracker.service.MailService;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import static org.mockito.Mockito.*;

/**
 * @author Martin Krumov
 */
@ExtendWith(MockitoExtension.class)
class MailServiceImplTest {

    private static final String TO_EMAIL = "user@mail.com";
    private static final String SOURCE_EMAIL = "uest@mail.email";
    private static final String FROM = "mee";

    @Spy
    private ITemplateEngine templateEngine;
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private IdpProperties idpProperties;
    @Mock
    private IdpProperties.MailProperties mailProperties;

    private MailMessage mailMessage;

    private MailService mailService;

    @BeforeEach
    void setUp() {
        mailService = new MailServiceImpl(mailSender, templateEngine, idpProperties);

        mailMessage = MailMessage.builder()
                .to(TO_EMAIL)
                .mailType(MailType.CONFIRM_EMAIL)
                .context(new Context())
                .build();

        when(idpProperties.getMail()).thenReturn(mailProperties);
        when(mailProperties.getEmail()).thenReturn(SOURCE_EMAIL);
        when(mailProperties.getFrom()).thenReturn(FROM);

        when(templateEngine.process(mailMessage.getMailType().getTemplate(), mailMessage.getContext()))
                .thenReturn("processed template :)");
    }

    @Test
    void sendEmailSuccessfullySendsEmail() {
        // arrange
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // act
        mailService.sendEmail(mailMessage);

        // assert
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
    }

}
