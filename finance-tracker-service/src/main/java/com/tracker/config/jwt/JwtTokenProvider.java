package com.tracker.config.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.*;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String USER_NAME = "user_name";

    @Value("${jwt.authorities.key}")
    private String authoritiesKey;

    @Value("${jwt.secret.key}")
    private String secretKey;

    public String createToken(Authentication authentication, Boolean rememberMe) {
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
                .setSubject(authentication.getName())
                .claim(USER_NAME, authentication.getName())
                .claim(authoritiesKey, authorities)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes()) //TODO: use RSA
                .compact();
    }

     Optional<Authentication> getAuthentication(String token) {
         Optional<Jws<Claims>> optionalJws = extractClaims(token);

         if (optionalJws.isEmpty()) {
             return empty();
         }

         Jws<Claims> jwt = optionalJws.get();

         Claims claims = jwt.getBody();

         String roles = claims.get(authoritiesKey).toString();

         String username = ofNullable(claims.getSubject())
                 .or(() -> ofNullable(claims.get(USER_NAME).toString()))
                 .orElseThrow(() -> new MissingClaimException(jwt.getHeader(), claims, "Subject not found."));

         Set<GrantedAuthority> authorities =
                 Arrays.stream(StringUtils.substring(roles, 1, roles.length() - 1).split(","))
                         .map(SimpleGrantedAuthority::new)
                         .collect(toSet());

         User principal = new User(username, "", authorities);

         return of(new UsernamePasswordAuthenticationToken(principal, "", authorities));
    }

    private Optional<Jws<Claims>> extractClaims(String authToken) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(authToken);

            return ofNullable(claims);
        } catch (SignatureException e) {
            log.warn("Invalid JWT signature: {} failed : {}", authToken, e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token: {} failed : {}", authToken, e.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token: {} failed : {}", authToken, e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token: {} failed : {}", authToken, e.getMessage());
        } catch (IllegalArgumentException exception) {
            log.warn("Request to parse empty or null JWT : {} failed : {}", authToken, exception.getMessage());
        }

        return empty();
    }
}

