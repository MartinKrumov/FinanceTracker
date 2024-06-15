package com.tracker;

import com.tracker.config.FinanceTrackerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@EnableConfigurationProperties(FinanceTrackerProperties.class)
public class FinanceTrackerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanceTrackerServiceApplication.class, args);
	}

}

