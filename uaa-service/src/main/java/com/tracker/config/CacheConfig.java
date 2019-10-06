package com.tracker.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
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
        Config config = new Config();

        config.setInstanceName(INSTANCE_NAME)
                .addMapConfig(
                        buildMapConfig(USERNAME_TO_LOGIN_ATTEMPTS,
                                -1,
                                new MaxSizeConfig(30, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_PERCENTAGE))
                )
                .addMapConfig(
                        buildMapConfig(USER_ROLES,
                                toIntExact(Duration.ofHours(12).toSeconds()),
                                new MaxSizeConfig(30, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_PERCENTAGE))
                );

        return config;
    }

    private MapConfig buildMapConfig(String name, Integer timeToLiveSeconds, MaxSizeConfig maxSizeConfig) {
        return new MapConfig()
                .setName(name)
                .setMaxSizeConfig(maxSizeConfig)
                .setEvictionPolicy(EvictionPolicy.LRU)
                .setTimeToLiveSeconds(timeToLiveSeconds);
    }
}
