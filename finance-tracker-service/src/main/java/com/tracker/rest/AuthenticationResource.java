//package com.tracker.rest;
//
//import com.tracker.config.jwt.JwtToken;
//import com.tracker.config.jwt.JwtTokenProvider;
//import com.tracker.rest.dto.user.LoginDTO;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.validation.Valid;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.BooleanUtils;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
//@Slf4j
//@RestController
//public class AuthenticationResource {
//
//    private static final String AUTHORIZATION_HEADER = "Authorization";
//
//    private final JwtTokenProvider jwtTokenProvider;
//    private final AuthenticationManager authenticationManager;
//    private final TestConfig testConfig;
//
//    public AuthenticationResource(JwtTokenProvider jwtTokenProvider,
//                                  AuthenticationManager authenticationManager,
//                                  TestConfig testConfig) {
//        this.jwtTokenProvider = jwtTokenProvider;
//        this.authenticationManager = authenticationManager;
//        this.testConfig = testConfig;
//    }
//
//    @PostMapping("/authenticate")
//    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse response) {
//
//        Authentication authenticationToken =
//                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
//
//        try {
//            Authentication authentication = authenticationManager.authenticate(authenticationToken);
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            boolean rememberMe = BooleanUtils.isTrue(loginDTO.getIsRememberMe());
//
//            String jwt = jwtTokenProvider.createToken(authentication, rememberMe);
//
//            response.addHeader(AUTHORIZATION_HEADER, "Bearer " + jwt);
//
//            return ResponseEntity.ok(new JwtToken(jwt));
//        } catch (AuthenticationException ae) {
//            //TODO: handle the exception in controller advice
//            log.warn("Authentication exception trace: {}", ae.getMessage());
//
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(Map.of("AuthenticationException", ae.getLocalizedMessage()));
//        }
//    }
//
//    @GetMapping("/demo")
//    public ResponseEntity<?> refreshDemo() {
//        log.info(testConfig.getReloadingMsg());
//        return ResponseEntity.ok(testConfig.getReloadingMsg());
//    }
//
//    @Data
//    @Configuration
//    @ConfigurationProperties("demo")
//    static class TestConfig {
//
//        private String reloadingMsg;
//    }
//
//}
//
