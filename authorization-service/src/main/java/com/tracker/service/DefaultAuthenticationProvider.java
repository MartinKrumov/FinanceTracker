package com.tracker.service;

import com.tracker.rest.dto.UserLoginDTO;
import com.tracker.proxy.FinanceTrackerServiceProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
@RequiredArgsConstructor
public class DefaultAuthenticationProvider implements AuthenticationProvider {

    private final FinanceTrackerServiceProxy financeTrackerServiceProxy;

    @Override
    public Authentication authenticate(final Authentication authentication) {
        String name = authentication.getName();
        Object credentials = authentication.getCredentials();

        if (isEmpty(name) && isNull(credentials)) {
            return null;
        }

        ResponseEntity<UserLoginDTO> response = financeTrackerServiceProxy.getUserByUsername(name);

        UserLoginDTO userLoginDTO = response.getBody();

        if (!response.getStatusCode().is2xxSuccessful() || isNull(userLoginDTO) || credentials.equals(userLoginDTO.getPassword())) {
            throw new UsernameNotFoundException("User not found");
        }

        return new UsernamePasswordAuthenticationToken(
                userLoginDTO.getEmail(),
                userLoginDTO.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
