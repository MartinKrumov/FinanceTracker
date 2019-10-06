package com.tracker.rest;

import com.tracker.config.AuthProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AdminResource {

    private final TokenStore tokenStore;
    private final AuthProperties authProperties;
    private final DefaultTokenServices tokenServices;

    public AdminResource(TokenStore tokenStore, AuthProperties authProperties, DefaultTokenServices tokenServices) {
        this.tokenStore = tokenStore;
        this.authProperties = authProperties;
        this.tokenServices = tokenServices;
    }

    @GetMapping("/admin/tokens")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<String>> findAllTokens() {
        final Collection<OAuth2AccessToken> tokensByClientId = tokenStore.findTokensByClientId(authProperties.getClient().getClientId());

        List<String> tokens = tokensByClientId.stream()
                .map(OAuth2AccessToken::getValue)
                .collect(Collectors.toList());

        return ResponseEntity.ok(tokens);
    }

    @DeleteMapping("/oauth/revoke")
    public ResponseEntity revokeToken(Authentication authentication) {
        final String userToken = ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
        boolean isRevoked = tokenServices.revokeToken(userToken);
        return ResponseEntity.ok(isRevoked);
    }

    @GetMapping("/hello")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> greetings(@RequestParam("name") String name, Principal principal) {
        String welcome = String.format("Welcome %s, principal: %s", name, principal);
        return ResponseEntity.ok(welcome);
    }
}
