package com.tracker.resources;

import com.tracker.dto.Welcome;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(name = "/api")
public class HelloResource {

    @GetMapping("/hello")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Welcome> greetings(@RequestParam("name") String name, Principal principal) {
        Welcome welcome = new Welcome(name + " (" + principal.getName() + ")");
        return ResponseEntity.ok(welcome);
    }
}
