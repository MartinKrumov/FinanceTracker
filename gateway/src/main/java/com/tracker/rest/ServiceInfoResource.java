package com.tracker.rest;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ServiceInfoResource {
    private final DiscoveryClient discoveryClient;

    public ServiceInfoResource(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("GatewayApplication says hello!");
    }

    @GetMapping("/description")
    public ResponseEntity<String> discoveryServiceDescription() {
        return ResponseEntity.ok(discoveryClient.description());
    }

    @GetMapping("/services")
    public ResponseEntity<List<String>> services() {
        return ResponseEntity.ok(discoveryClient.getServices());
    }

    @GetMapping("/services/{serviceId}")
    public ResponseEntity<List<ServiceInstance>> serviceById(@PathVariable String serviceId) {
        return ResponseEntity.ok(discoveryClient.getInstances(serviceId));
    }
}
