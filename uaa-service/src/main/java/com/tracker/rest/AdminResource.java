package com.tracker.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class AdminResource {


    @GetMapping("/hello")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> greetings(@RequestParam String name, Principal principal) {
        String welcome = "Welcome %s, principal: %s".formatted(name, principal);
        return ResponseEntity.ok(welcome);
    }
}
