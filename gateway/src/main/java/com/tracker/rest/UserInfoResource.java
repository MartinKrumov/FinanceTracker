package com.tracker.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserInfoResource {

    @GetMapping("/userinfo")
    public ResponseEntity<Map<String, Object>> userInfo(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
            @AuthenticationPrincipal OAuth2User oauth2User) {
        Map<String, Object> model = new HashMap<>();
        model.put("clientName", authorizedClient.getClientRegistration().getClientName());
        model.put("userName", oauth2User.getName());
        model.put("userAttributes", oauth2User.getAttributes());
        return ResponseEntity.ok(model);
    }
}
