package com.tracker.config;

import com.tracker.config.security.SpringSecurityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaConfig {

    private final SpringSecurityAuditorAware auditorAware;

    public JpaConfig(SpringSecurityAuditorAware auditorAware) {
        this.auditorAware = auditorAware;
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return auditorAware;
    }
}
