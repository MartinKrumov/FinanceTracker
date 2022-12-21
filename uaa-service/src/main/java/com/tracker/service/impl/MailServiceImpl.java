package com.tracker.service.impl;

import com.tracker.common.util.mail.MailMessage;
import com.tracker.config.IdpProperties;
import com.tracker.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    private static final String CONTENT_ID_LOGO = "logo";
    private static final String CONTENT_TYPE_IMAGE_PNG = "image/png";
    private static final String MAIL_LOGO_PNG_PATH = "static/mail/logo.png";

    private final JavaMailSender mailSender;
    private final ITemplateEngine templateEngine;
    private final IdpProperties idpProperties;

    public MailServiceImpl(JavaMailSender mailSender, ITemplateEngine templateEngine, IdpProperties idpProperties) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.idpProperties = idpProperties;
    }

    @Async
    @Override
    public void sendEmail(MailMessage mailMessage) {
        log.info("Preparing {} email to: {}", mailMessage.getMailType().name(), mailMessage.getTo());
        MimeMessage message = mailSender.createMimeMessage();

        try {
            Context context = mailMessage.getContext();
            context.setVariable(CONTENT_ID_LOGO, CONTENT_ID_LOGO); // so that we can reference it

            String content = processTemplate(mailMessage.getMailType().getTemplate(), context);

            buildMimeMessageHelper(mailMessage, message, content);

            mailSender.send(message);
            log.debug("Email sent to: {}", mailMessage.getTo());
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new IllegalStateException("The email cannot be sent, to: " + mailMessage.getTo(), e);
        }
    }

    /**
     * Builds mime message helper.
     *
     * @param mailMessage {@link MailMessage}
     * @param message     {@link MimeMessage}
     * @param content     the processed template
     * @throws MessagingException if multipart creation failed
     */
    private void buildMimeMessageHelper(MailMessage mailMessage, MimeMessage message, String content)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
        messageHelper.setTo(mailMessage.getTo());
        messageHelper.setFrom(idpProperties.getMail().getEmail(), idpProperties.getMail().getFrom());
        messageHelper.setSubject(mailMessage.getMailType().getSubject());
        messageHelper.setText(content, true);
        messageHelper.addInline(CONTENT_ID_LOGO, new ClassPathResource(MAIL_LOGO_PNG_PATH), CONTENT_TYPE_IMAGE_PNG);
    }

    /**
     * Process email template.
     *
     * @param templateName the template name
     * @param context      the content ({@link Context})
     * @return the result of evaluating the specified template with the provided context
     */
    private String processTemplate(String templateName, Context context) {
        return templateEngine.process(templateName, context);
    }
}
