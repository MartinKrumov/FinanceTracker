package com.tracker.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/oauth")
public class TokenResource {

    private final DefaultTokenServices tokenServices;

    public TokenResource(DefaultTokenServices tokenServices) {
        this.tokenServices = tokenServices;
    }

    @DeleteMapping("/revoke")
    public ResponseEntity revokeToken(Authentication authentication) {
        final String userToken = ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
        boolean isRevoked = tokenServices.revokeToken(userToken);
        return ResponseEntity.ok(isRevoked);
    }
}
