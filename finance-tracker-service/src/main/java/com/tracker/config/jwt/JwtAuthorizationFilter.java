package com.tracker.config.jwt;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.*;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends GenericFilterBean {

    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

            String jwt = resolveToken(httpServletRequest);

            jwtTokenProvider.getAuthentication(jwt)
                    .ifPresent(auth -> SecurityContextHolder.getContext().setAuthentication(auth));

        } catch (JwtException jwtException) {
            log.warn("Security jwtException: {}", jwtException.getMessage());
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveToken(HttpServletRequest request) {
        return ofNullable(request.getHeader(AUTHORIZATION_HEADER))
                .filter(this::isValidBearerToken)
                .map(token -> substringAfter(token, BEARER))
                .orElseThrow(() -> new JwtException("Jwt token not present."));
    }

    private Boolean isValidBearerToken(String token) {
        return contains(token, BEARER) && isNotBlank(substringAfter(token, BEARER));
    }
}
