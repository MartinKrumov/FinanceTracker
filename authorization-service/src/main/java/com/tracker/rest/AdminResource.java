package com.tracker.rest;

import com.tracker.config.AuthProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminResource {

    private final TokenStore tokenStore;
    private final AuthProperties authProperties;

    public AdminResource(TokenStore tokenStore, AuthProperties authProperties) {
        this.tokenStore = tokenStore;
        this.authProperties = authProperties;
    }

    @GetMapping("/token/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<String>> findAllTokens() {
        final Collection<OAuth2AccessToken> tokensByClientId = tokenStore.findTokensByClientId(authProperties.getClient().getClientId());

        List<String> tokens = tokensByClientId.stream()
                .map(OAuth2AccessToken::getValue)
                .collect(Collectors.toList());

        return ResponseEntity.ok(tokens);
    }
}
