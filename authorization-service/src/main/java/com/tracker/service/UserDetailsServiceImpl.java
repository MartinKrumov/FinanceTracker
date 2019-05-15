package com.tracker.service;

import com.tracker.dto.UserLoginDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static java.util.Objects.isNull;
import static org.springframework.security.core.userdetails.User.withUsername;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String GET_USER_URL = "http://localhost:8090/api/users/%s";

    private final RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) {
        String url = String.format(GET_USER_URL, username);

        ResponseEntity<UserLoginDTO> response =
                restTemplate.getForEntity(url, UserLoginDTO.class);

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
