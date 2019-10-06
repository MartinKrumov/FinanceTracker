package com.tracker.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    private SecurityUtils() {
        throw new IllegalStateException("This is Utility class.");
    }

    /**
     * Get the authentication from {@link SecurityContextHolder}
     *
     * @return {@link Optional} of {@link Authentication}
     */
    public static Optional<Authentication> getAuthentication() {
        return ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * Get the username of the current user.
     *
     * @return the username of the current user.
     */
    public static Optional<String> getCurrentUser() {
        return getAuthentication()
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getName);
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    public static boolean isAuthenticated() {
        return getAuthentication()
                .map(authentication -> authentication.getAuthorities().stream()
                        .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equalsIgnoreCase("anonymousUser")))
                .orElse(false);
    }

    /**
     * If the current user has a specific authority (security role).
     * <p>
     * The name of this method comes from the {@code isUserInRole()} method in the Servlet API.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
    public static boolean isCurrentUserInRole(String authority) {
        return getAuthentication()
                .map(authentication -> authentication.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority)))
                .orElse(false);
    }
}
