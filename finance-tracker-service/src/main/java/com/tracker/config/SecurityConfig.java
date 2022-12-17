package com.tracker.config;

import com.tracker.config.keycloak.KeycloakRealmRoleConverter;
import com.tracker.config.keycloak.UsernameSubClaimAdapter;
import com.tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.context.ShutdownEndpoint;
import org.springframework.boot.actuate.metrics.export.prometheus.PrometheusScrapeEndpoint;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.stream;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final Set<String> ALLOWED_HEADERS =
            Set.of("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization");

    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/v2/api-docs",
            "/config/ui",
            "/swagger-resources/**",
            "/config/**",
            "/swagger-ui.html",
            "/webjars/**"
            // other public endpoints
    };

    private final UserService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final String corsOrigins;

    @Autowired
    public SecurityConfig(UserService userDetailsService, PasswordEncoder passwordEncoder,
                          FinanceTrackerProperties financeTrackerProperties) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.corsOrigins = financeTrackerProperties.getCorsOrigins();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
         var daoAuthenticationProvider = new DaoAuthenticationProvider();
         daoAuthenticationProvider.setUserDetailsService(userDetailsService);
         daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
         return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers(AUTH_WHITELIST);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authorizeRequests -> authorizeRequests
                    .requestMatchers(EndpointRequest.to(ShutdownEndpoint.class))
                        .hasRole("ADMIN")
                    .requestMatchers(EndpointRequest.toAnyEndpoint().excluding(PrometheusScrapeEndpoint.class))
                        .permitAll()
                    .antMatchers("/users/register", "/authenticate", "/users/{username}")
                        .permitAll()
                    .anyRequest().authenticated())
            .sessionManagement(sessionManagement ->
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Validate tokens through configured OpenID Provider
            .oauth2ResourceServer(oAuth2ResourceServer -> oAuth2ResourceServer
                    .jwt().jwtAuthenticationConverter(jwtAuthenticationConverter())
            )
            .exceptionHandling(exceptionHandling -> exceptionHandling
//                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                    .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
            )
            .csrf().disable()
            .cors(withDefaults());
//              Custom AuthenticationFilter
//              .addFilterBefore(new JwtAuthorizationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    @Bean
//    public JwtAuthorizationFilter jwtAuthorizationFilter() {
//        Use new .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
//        return new JwtAuthorizationFilter(jwtTokenProvider);
//    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        List<String> allowedMethods = stream(HttpMethod.values())
                .map(Enum::name)
                .toList();

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(corsOrigins));
        configuration.setAllowedMethods(allowedMethods);
        configuration.setAllowedHeaders(new ArrayList<>(ALLOWED_HEADERS));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        // Convert realm_access.roles claims to granted authorities, for use in access decisions
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return jwtAuthenticationConverter;
    }

}
