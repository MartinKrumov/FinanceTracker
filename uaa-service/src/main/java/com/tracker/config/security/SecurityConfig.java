package com.tracker.config.security;

import com.tracker.common.FilterChainExceptionHandlingFilter;
import com.tracker.config.security.keycloak.KeycloakRealmRoleConverter;
import com.tracker.config.security.keycloak.UsernameSubClaimAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.context.ShutdownEndpoint;
import org.springframework.boot.actuate.metrics.export.prometheus.PrometheusScrapeEndpoint;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/v2/api-docs",
            "/config/ui",
            "/swagger-resources/**",
            "/config/**",
            "/swagger-ui.html",
            "/webjars/**"
    };

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final FilterChainExceptionHandlingFilter filterChainExceptionHandlingFilter;

    @Autowired
    public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, PasswordEncoder passwordEncoder,
                          FilterChainExceptionHandlingFilter filterChainExceptionHandlingFilter) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.filterChainExceptionHandlingFilter = filterChainExceptionHandlingFilter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(AUTH_WHITELIST);
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            // Validate tokens through configured OpenID Provider
            .oauth2ResourceServer(oAuth2ResourceServer ->
                    oAuth2ResourceServer
                            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            )
            .authorizeRequests(authorizeRequests ->
                    authorizeRequests
                            .requestMatchers(EndpointRequest.to(ShutdownEndpoint.class))
                                .hasRole("ADMIN")
                            .requestMatchers(EndpointRequest.toAnyEndpoint().excluding(PrometheusScrapeEndpoint.class))
                                .permitAll()
                            .antMatchers("/api/users/register", "/oauth/**").permitAll()
                            .anyRequest().authenticated()
            )
            .csrf().disable()
            .addFilterBefore(filterChainExceptionHandlingFilter, LogoutFilter.class)
            .sessionManagement(sessionManagement ->
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .exceptionHandling(exceptionHandling ->
                    exceptionHandling.accessDeniedHandler(new OAuth2AccessDeniedHandler())
            );
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
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation(issuerUri);

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

