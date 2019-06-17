package com.tracker.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Default Bean config.
 */
@Configuration
public class BeanConfig {

    /**
     * Create model mapper.
     *
     * @return {@link ModelMapper}
     */
    @Bean
    public ModelMapper createMapper() {
        return new ModelMapper();
    }

    /**
     * Default been for {@link PasswordEncoder}
     *
     * @return {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
