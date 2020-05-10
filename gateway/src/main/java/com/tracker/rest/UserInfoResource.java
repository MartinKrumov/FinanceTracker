package com.tracker.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserInfoResource {

    @GetMapping("/userinfo")
    public ResponseEntity<Map<String, Object>> userInfo(@AuthenticationPrincipal OAuth2User oauth2User) {
        Map<String, Object> model = new HashMap<>();
        model.put("userName", oauth2User.getName());
        model.put("userAttributes", oauth2User.getAttributes());
        return ResponseEntity.ok(model);
    }

    @GetMapping("/headers")
    public ResponseEntity<?> headers(ServerHttpRequest serverHttpRequest) {
        return ResponseEntity.ok(serverHttpRequest.getHeaders().values());
    }
}
