package com.tracker.config.security;

import com.tracker.config.AuthProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import static java.lang.Math.toIntExact;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final TokenStore tokenStore;
    private final PasswordEncoder passwordEncoder;
    private final AuthProperties authProperties;
    private final AuthenticationManager authenticationManager;
    private final JwtAccessTokenConverter jwtAccessTokenConverter;

    public AuthorizationServerConfig(TokenStore tokenStore, PasswordEncoder passwordEncoder, AuthProperties authProperties,
                                     AuthenticationManager authenticationManager, @Qualifier("accessTokenConverter") JwtAccessTokenConverter jwtAccessTokenConverter) {
        this.tokenStore = tokenStore;
        this.passwordEncoder = passwordEncoder;
        this.authProperties = authProperties;
        this.authenticationManager = authenticationManager;
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        AuthProperties.Client client = authProperties.getClient();
        clients
                .inMemory()
                .withClient(client.getClientId())
                .secret(passwordEncoder.encode(client.getClientSecret()))
                .authorizedGrantTypes(client.getGrantTypes().toArray(String[]::new))
                .scopes(client.getScope().toArray(String[]::new))
                .accessTokenValiditySeconds(toIntExact(authProperties.getJwt().getValidity().getSeconds()))
                .refreshTokenValiditySeconds(toIntExact(authProperties.getJwt().getRefreshValidity().toSeconds()));
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore)
                .accessTokenConverter(jwtAccessTokenConverter)
                .authenticationManager(authenticationManager);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }
}
