package com.tracker.config.security;

import com.tracker.config.AuthClientProperties;
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

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private static final int VALID_SECONDS = 7200;
    private static final int REFRESH_TOKEN_VALID_SECONDS = 86400;

    private final TokenStore tokenStore;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final AuthClientProperties authClientProperties;
    private final AuthenticationManager authenticationManager;
    private final JwtAccessTokenConverter jwtAccessTokenConverter;

    public AuthorizationServerConfig(TokenStore tokenStore, PasswordEncoder bCryptPasswordEncoder, AuthClientProperties authClientProperties,
                                     AuthenticationManager authenticationManager, @Qualifier("accessTokenConverter") JwtAccessTokenConverter jwtAccessTokenConverter) {
        this.tokenStore = tokenStore;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authClientProperties = authClientProperties;
        this.authenticationManager = authenticationManager;
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        AuthClientProperties.Client client = authClientProperties.getClient();
        clients
                .inMemory()
                .withClient(client.getClientId())
                .secret(bCryptPasswordEncoder.encode(client.getClientSecret()))
                .authorizedGrantTypes(client.getGrantTypes().toArray(String[]::new))
                .scopes(client.getScope().toArray(String[]::new))
                .accessTokenValiditySeconds(VALID_SECONDS)
                .refreshTokenValiditySeconds(REFRESH_TOKEN_VALID_SECONDS);
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
