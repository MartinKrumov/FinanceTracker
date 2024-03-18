package com.tracker.config.security;

import com.tracker.common.FilterChainExceptionHandlingFilter;
import com.tracker.config.security.keycloak.KeycloakRealmRoleConverter;
import com.tracker.config.security.keycloak.UsernameSubClaimAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.context.ShutdownEndpoint;
import org.springframework.boot.actuate.metrics.export.prometheus.PrometheusScrapeEndpoint;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html"
            // other public endpoints
    };

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final FilterChainExceptionHandlingFilter filterChainExceptionHandlingFilter;

    public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, PasswordEncoder passwordEncoder,
                          FilterChainExceptionHandlingFilter filterChainExceptionHandlingFilter) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.filterChainExceptionHandlingFilter = filterChainExceptionHandlingFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        var daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests ->
                    authorizeRequests
                            .requestMatchers(EndpointRequest.to(ShutdownEndpoint.class))
                                .hasRole("ADMIN")
                            .requestMatchers(EndpointRequest.toAnyEndpoint().excluding(PrometheusScrapeEndpoint.class))
                                .permitAll()
                            .requestMatchers("/api/users/register", "/oauth/**")
                                .permitAll()
                            .requestMatchers(AUTH_WHITELIST)
                                .permitAll()
                            .anyRequest().authenticated()
            )
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .oauth2ResourceServer(oAuth2ResourceServer ->
                    // Validate tokens through configured OpenID Provider
                    oAuth2ResourceServer.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            )
            .exceptionHandling(exceptionHandling -> exceptionHandling
                    .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                    .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
            )
            .addFilterBefore(filterChainExceptionHandlingFilter, LogoutFilter.class)
            .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        // Convert realm_access.roles claims to granted authorities, for use in access decisions
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return jwtAuthenticationConverter;
    }

    @Bean
    public JwtDecoder jwtDecoderByIssuerUri(OAuth2ResourceServerProperties properties) {
        String issuerUri = properties.getJwt().getIssuerUri();
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuerUri);

        //TODO: fix issuer problem in docker/k8s
        OAuth2TokenValidator<Jwt> customIssuerValidator = (Jwt token) -> OAuth2TokenValidatorResult.success();

        DelegatingOAuth2TokenValidator<Jwt> jwtValidator =
                new DelegatingOAuth2TokenValidator<>(JwtValidators.createDefault(), customIssuerValidator);

        jwtDecoder.setJwtValidator(jwtValidator);
        // Use preferred_username from claims as authentication name, instead of UUID subject
        jwtDecoder.setClaimSetConverter(new UsernameSubClaimAdapter());
        return jwtDecoder;
    }
}

