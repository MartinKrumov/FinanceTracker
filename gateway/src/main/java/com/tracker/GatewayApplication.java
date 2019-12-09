package com.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@EnableDiscoveryClient
@SpringBootApplication
@Controller // TODO: remove
public class GatewayApplication {

    @GetMapping(value = "/")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/userinfo")
    @ResponseBody
    public Map<String, Object> index(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
            @AuthenticationPrincipal OAuth2User oauth2User) {
        Map<String, Object> model = new HashMap<>();
        model.put("clientName", authorizedClient.getClientRegistration().getClientName());
        model.put("userName", oauth2User.getName());
        model.put("userAttributes", oauth2User.getAttributes());
        return model;
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
