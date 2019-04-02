package com.tracker.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "/api")
public class HelloResource {

    @GetMapping("/hello")
    public ResponseEntity justTrying() {
        return ResponseEntity.ok("Just trying to implement oAuth2");
    }
}
