package com.tracker.config;

import com.hazelcast.config.*;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static java.lang.Math.toIntExact;

@EnableCaching
@Configuration
public class CacheConfig {

    private static final String INSTANCE_NAME = "auth-service";
    private static final String USERNAME_TO_LOGIN_ATTEMPTS = "usernameToLoginAttempts";
    private static final String USER_ROLES = "roles";

    @Bean
    public Config hazelCastConfig() {
        return new Config()
                .setInstanceName(INSTANCE_NAME)
                .addMapConfig(
                        buildMapConfig(USERNAME_TO_LOGIN_ATTEMPTS, -1, buildEvictionConfig())
                )
                .addMapConfig(
                        buildMapConfig(USER_ROLES, toIntExact(Duration.ofHours(12).toSeconds()), buildEvictionConfig())
                );
    }

    private EvictionConfig buildEvictionConfig() {
        return new EvictionConfig()
                .setSize(30)
                .setMaxSizePolicy(MaxSizePolicy.FREE_HEAP_PERCENTAGE)
                .setEvictionPolicy(EvictionPolicy.LRU);
    }

    private MapConfig buildMapConfig(String name, Integer timeToLiveSeconds, EvictionConfig evictionConfig) {
        return new MapConfig()
                .setName(name)
                .setEvictionConfig(evictionConfig)
                .setTimeToLiveSeconds(timeToLiveSeconds);
    }
}
