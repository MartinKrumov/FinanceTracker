package com.tracker.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@TestConfiguration(proxyBeanMethods = false)
public class ContainerConfig {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSQLContainer() {
        return new PostgreSQLContainer<>("postgres:16-alpine")
                .withDatabaseName("finance_tracker")
                .withUsername("postgres")
                .withPassword("admin")
                .withExposedPorts(5432)
                .waitingFor(Wait.forListeningPort());
    }

}
