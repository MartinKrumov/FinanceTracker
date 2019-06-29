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

import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String USER_NAME = "user_name";
    private static final long TOKEN_VALIDITY_IN_MILLISECONDS = 50_100_100L;
    private static final long TOKEN_VALIDITY_IN_MILLISECONDS_FOR_REMEMBER_ME = 1_000_100_100L;

    @Value("${jwt.authorities.key}")
    private String authoritiesKey;

    @Value("${jwt.secret.key}")
    private String secretKey;



    public String createToken(Authentication authentication, Boolean rememberMe) {
        Set<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(toSet());

        long now = new Date().getTime();
        Date validity;

        if (rememberMe) {
            validity = new Date(now + TOKEN_VALIDITY_IN_MILLISECONDS_FOR_REMEMBER_ME);
        } else {
            validity = new Date(now + TOKEN_VALIDITY_IN_MILLISECONDS);
        }

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(USER_NAME, authentication.getName())
                .claim(authoritiesKey, authorities)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

     Authentication getAuthentication(String token) {

        @SuppressWarnings("unchecked")
        Jwt<Header, Claims> jwt = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parse(token);

        Claims claims = jwt.getBody();

        String roles = claims.get(authoritiesKey).toString();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(StringUtils.substring(roles,1, roles.length() - 1).split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(toList());

        String username = Optional.ofNullable(claims.getSubject())
                .or(() -> Optional.ofNullable(claims.get(USER_NAME).toString()))
                .orElseThrow(() -> new MissingClaimException(jwt.getHeader(), jwt.getBody(), "Subject not found."));

        User principal = new User(username, "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(authToken);
            return true;
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

        return false;
    }
}

