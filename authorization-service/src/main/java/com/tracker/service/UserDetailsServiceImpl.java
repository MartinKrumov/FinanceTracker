package com.tracker.service;

import com.tracker.dto.UserLoginDTO;
import com.tracker.proxy.FinanceTrackerServiceProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;
import static org.springframework.security.core.userdetails.User.withUsername;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final FinanceTrackerServiceProxy financeTrackerServiceProxy;

    @Override
    public UserDetails loadUserByUsername(String username) {
        ResponseEntity<UserLoginDTO> response = financeTrackerServiceProxy.getUserByUsername(username);

        if (!response.getStatusCode().is2xxSuccessful() || isNull(response.getBody())) {
            throw new UsernameNotFoundException("User not found");
        }

        UserLoginDTO user = response.getBody();

        return withUsername(username)
                .password(user.getPassword())
                .roles(user.getAuthorities().toArray(String[]::new))
                .build();
    }
}
