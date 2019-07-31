package com.tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.nio.charset.StandardCharsets;

/**
 * Thymeleaf config.
 */
@Configuration
public class ThymeleafConfig {

    private static final String HTML_SUFFIX = ".html";
    private static final String TEMPLATES_PREFIX = "classpath:/templates/";

    /**
     * Spring template engine template engine.
     *
     * @return the template engine
     */
    @Bean
    public TemplateEngine springTemplateEngine() {
        TemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        return templateEngine;
    }

    /**
     * Html template resolver.
     *
     * @return the template resolver
     */
    @Bean
    public ITemplateResolver htmlTemplateResolver() {
        SpringResourceTemplateResolver emailTemplateResolver = new SpringResourceTemplateResolver();
        emailTemplateResolver.setPrefix(TEMPLATES_PREFIX);
        emailTemplateResolver.setSuffix(HTML_SUFFIX);
        emailTemplateResolver.setTemplateMode(TemplateMode.HTML);
        emailTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return emailTemplateResolver;
    }
}
