package com.tracker.rest;

import com.tracker.config.AuthClientProperties;
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
@RequestMapping(value = "/admin")
public class AdminResource {

    private final TokenStore tokenStore;
    private final AuthClientProperties authClientProperties;

    public AdminResource(TokenStore tokenStore, AuthClientProperties authClientProperties) {
        this.tokenStore = tokenStore;
        this.authClientProperties = authClientProperties;
    }

    @GetMapping("/token/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<String>> findAllTokens() {
        final Collection<OAuth2AccessToken> tokensByClientId = tokenStore.findTokensByClientId(authClientProperties.getClient().getClientId());

        List<String> tokens = tokensByClientId.stream()
                .map(OAuth2AccessToken::getValue)
                .collect(Collectors.toList());

        return ResponseEntity.ok(tokens);
    }
}
