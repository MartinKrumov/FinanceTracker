package com.tracker.config.jwt;


import com.tracker.config.FinanceTrackerProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static java.util.Arrays.stream;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.substring;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String USER_NAME = "user_name";
    private static final Set<String> PRINCIPAL_KEYS = Set.of(
            USER_NAME,
            "sub",
            "user",
            "username",
            "userid",
            "user_id",
            "login",
            "id",
            "name"
    );

    private final FinanceTrackerProperties.JwtProperties jwtProperties;

    public JwtTokenProvider(FinanceTrackerProperties financeTrackerProperties) {
        this.jwtProperties = financeTrackerProperties.getJwtProperties();
    }

    public String createToken(Authentication authentication, boolean rememberMe) {
        Set<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(toSet());

        long now = Instant.now().toEpochMilli();
        Date validity;

        if (rememberMe) {
            validity = new Date(Math.addExact(now, Duration.ofDays(30).toMillis()));
        } else {
            validity = new Date(Math.addExact(now, Duration.ofMinutes(30).toMillis()));
        }

        return Jwts.builder()
                .setHeader((Map<String, Object>) Jwts.jwsHeader().setType("JWT"))
                .setSubject(authentication.getName())
                .claim(USER_NAME, authentication.getName())
                .claim(jwtProperties.getAuthoritiesKey(), authorities)
                .setExpiration(validity)
                .setIssuedAt(new Date(now))
                .setId(UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getJwtSecret().getBytes()) //TODO: use RSA
                .compact();
    }

    Optional<Authentication> getAuthentication(String token) {
        try {
            Jws<Claims> jwt = extractClaims(token);

            return of(buildAuthentication(jwt));
        } catch (SignatureException e) {
            log.warn("Invalid JWT signature: {} failed : {}", token, e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token: {} failed : {}", token, e.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token: {} failed : {}", token, e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token: {} failed : {}", token, e.getMessage());
        } catch (IllegalArgumentException exception) {
            log.warn("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
        }

        return empty();
    }

    private Authentication buildAuthentication(Jws<Claims> jwt) {
        Claims claims = jwt.getBody();

        String username = extractPrincipal(claims)
                .orElseThrow(() -> new MissingClaimException(jwt.getHeader(), claims, "Subject not found."));

        String roles = claims.get(jwtProperties.getAuthoritiesKey()).toString();

        Set<GrantedAuthority> authorities =
                stream(substring(roles, 1, roles.length() - 1).split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(toSet());

        User principal = new User(username, "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * Extract the principal that should be used for the token.
     *
     * @param claims the source map
     * @return {@link Optional} containing the extracted principal or {@link Optional#empty()}
     */
    private Optional<String> extractPrincipal(Map<String, Object> claims) {
        return PRINCIPAL_KEYS.stream()
                .filter(claims::containsKey)
                .findFirst()
                .map(key -> (String) claims.get(key));
    }

    /**
     * Extract claims from jwt token.
     *
     * @param authToken the auth token
     * @return {@link Jws} containing the {@link Claims}
     */
    private Jws<Claims> extractClaims(String authToken) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getJwtSecret().getBytes())
                .parseClaimsJws(authToken);
    }
}

