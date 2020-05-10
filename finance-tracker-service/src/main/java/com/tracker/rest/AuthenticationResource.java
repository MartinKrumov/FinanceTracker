package com.tracker.rest;

import com.tracker.config.jwt.JwtToken;
import com.tracker.config.jwt.JwtTokenProvider;
import com.tracker.rest.dto.user.LoginDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationResource {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/authenticate")
    public ResponseEntity authenticate(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse response) {

        Authentication authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            boolean rememberMe = BooleanUtils.isTrue(loginDTO.getIsRememberMe());

            String jwt = jwtTokenProvider.createToken(authentication, rememberMe);

            response.addHeader(AUTHORIZATION_HEADER, "Bearer " + jwt);

            return ResponseEntity.ok(new JwtToken(jwt));
        } catch (AuthenticationException ae) {
            //TODO: handle the exception in controller advice
            log.warn("Authentication exception trace: {}", ae.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("AuthenticationException", ae.getLocalizedMessage()));
        }
    }
}

