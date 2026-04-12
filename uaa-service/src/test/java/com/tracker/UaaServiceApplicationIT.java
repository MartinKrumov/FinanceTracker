package com.tracker;

import com.tracker.config.ContainerConfig;
import com.tracker.security.LoginAttemptComponent;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;

@ActiveProfiles("test")
@Import(ContainerConfig.class)
@SpringBootTest
class UaaServiceApplicationIT {

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @MockitoBean
    private LoginAttemptComponent loginAttemptComponent;

    @Test
    void contextLoads() {
        //verify context run
    }

}
