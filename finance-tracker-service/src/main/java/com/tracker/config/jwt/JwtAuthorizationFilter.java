package com.tracker.config.jwt;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends GenericFilterBean {

    private static final String BEARER = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

            String jwt = resolveToken(httpServletRequest)
                    .filter(StringUtils::hasText)
                    .orElseThrow(() -> new JwtException("Jwt token not present."));

            if (jwtTokenProvider.validateToken(jwt)) {
                Authentication authentication = this.jwtTokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtException jwtException) {
            log.warn("Security jwtException: {}", jwtException.getMessage());
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private Optional<String> resolveToken(HttpServletRequest request) {
        return ofNullable(request.getHeader(JwtConfigurer.AUTHORIZATION_HEADER))
                .filter(bearerToken -> StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER))
                .map(bearerToken ->
                        bearerToken.substring(BEARER.length())
                );
    }
}
