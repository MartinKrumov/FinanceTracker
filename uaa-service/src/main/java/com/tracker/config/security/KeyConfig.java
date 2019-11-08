package com.tracker.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

/**
 * An Authorization Server will more typically have a key rotation strategy. Should be implemented soon.
 */
@Configuration
class KeyConfig {

    @Bean
    public KeyPair keyPair() {
        //TODO: Externalize
        KeyStoreKeyFactory keyStoreKeyFactory =
                new KeyStoreKeyFactory(new ClassPathResource("mykeys.jks"), "mypass".toCharArray());
        return keyStoreKeyFactory.getKeyPair("mykeys");
    }
}
