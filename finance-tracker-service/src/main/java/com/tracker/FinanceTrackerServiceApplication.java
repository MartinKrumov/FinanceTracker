package com.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class FinanceTrackerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanceTrackerServiceApplication.class, args);
	}

}

