package com.tracker.service;

import com.tracker.model.AppUser;
import com.tracker.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Service
public class DefaultAuthenticationProvider implements AuthenticationProvider {

    private final AppUserRepository appUserRepository;

    @Autowired
    public DefaultAuthenticationProvider(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) {
        String name = authentication.getName();
        Object credentials = authentication.getCredentials();

        if (isEmpty(name) && isNull(credentials)) {
            return null;
        }

        AppUser user = appUserRepository.findById(name)
                .filter(appUser -> credentials.equals(appUser.getUserPass()))
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password."));

        return new UsernamePasswordAuthenticationToken(
                user.getUserEmail(),
                user.getUserPass(),
                Collections.singleton(new SimpleGrantedAuthority(user.getUserRole())));
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
