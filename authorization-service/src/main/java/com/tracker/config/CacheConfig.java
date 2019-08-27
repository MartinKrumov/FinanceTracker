package com.tracker.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig {

    private static final String INSTANCE_NAME = "auth-service";
    private static final String USERNAME_TO_LOGIN_ATTEMPTS = "usernameToLoginAttempts";

    @Bean
    public Config hazelCastConfig() {
        Config config = new Config();

        config.setInstanceName(INSTANCE_NAME)
                .addMapConfig(
                        buildMapConfig(USERNAME_TO_LOGIN_ATTEMPTS, new MaxSizeConfig(30, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_PERCENTAGE), -1)
                );

        return config;
    }

    private MapConfig buildMapConfig(String name, MaxSizeConfig maxSizeConfig, Integer timeToLiveSeconds) {
        return new MapConfig()
                .setName(name)
                .setMaxSizeConfig(maxSizeConfig)
                .setEvictionPolicy(EvictionPolicy.LRU)
                .setTimeToLiveSeconds(timeToLiveSeconds);
    }
}
