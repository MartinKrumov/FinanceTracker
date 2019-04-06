package com.tracker.service;

import com.tracker.dto.UserLoginDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toSet;

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

        UserLoginDTO userLoginDTO = response.getBody();

        return new User(userLoginDTO.getEmail(),
                passwordNoEncoding(userLoginDTO.getPassword()),
                getSimpleGrantedAuthorities(userLoginDTO.getAuthorities()));
    }

    private Set<SimpleGrantedAuthority> getSimpleGrantedAuthorities(Set<String> authorities) {
        return authorities.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(toSet());
    }

    private String passwordNoEncoding(String passwrod) {
        // you can use one of bcrypt/noop/pbkdf2/scrypt/sha256
        // more: https://spring.io/blog/2017/11/01/spring-security-5-0-0-rc1-released#password-encoding
        return "{noop}" + passwrod;
    }
}
