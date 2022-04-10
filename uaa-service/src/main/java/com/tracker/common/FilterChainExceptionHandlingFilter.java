package com.tracker.common;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Exception handling filter that propagates any filter chain exceptions
 * to the {@link GlobalExceptionHandler}
 */
@Component
public class FilterChainExceptionHandlingFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver exceptionResolver;

    public FilterChainExceptionHandlingFilter(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,  HttpServletResponse response, FilterChain filterChain) {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            exceptionResolver.resolveException(request, response, null, ex);
        }
    }
}
