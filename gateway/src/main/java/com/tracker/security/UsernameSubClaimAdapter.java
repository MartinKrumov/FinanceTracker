package com.tracker.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;

import java.util.Collections;
import java.util.Map;

/**
 * As per: <a href="https://docs.spring.io/spring-security/site/docs/5.2.x/reference/html5/#oauth2resourceserver-jwt-claimsetmapping-rename">Docs</a>
 */
public class UsernameSubClaimAdapter implements Converter<Map<String, Object>, Map<String, Object>> {

    private final MappedJwtClaimSetConverter delegate = MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());

    @Override
    public Map<String, Object> convert(Map<String, Object> claims) {
        Map<String, Object> convertedClaims = this.delegate.convert(claims);
        convertedClaims.put("sub", convertedClaims.get("preferred_username"));
        return convertedClaims;
    }

}
