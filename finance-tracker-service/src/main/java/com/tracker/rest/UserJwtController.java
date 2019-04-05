package com.tracker.rest;

import com.tracker.config.jwt.JwtConfigurer;
import com.tracker.config.jwt.JwtToken;
import com.tracker.config.jwt.JwtTokenProvider;
import com.tracker.dto.user.LoginModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserJwtController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/authenticate")
    public ResponseEntity authorize(@Valid @RequestBody LoginModel loginModel, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginModel.getUsername(), loginModel.getPassword());

        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            boolean rememberMe = Objects.isNull(loginModel.getIsRememberMe()) ? false : loginModel.getIsRememberMe();

            String jwt = jwtTokenProvider.createToken(authentication, rememberMe);

            response.addHeader(JwtConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);

            return ResponseEntity.ok(new JwtToken(jwt));
        } catch (AuthenticationException ae) {
            log.info("Authentication exception trace: {}", ae);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("AuthenticationException", ae.getLocalizedMessage()));
        }
    }
}
