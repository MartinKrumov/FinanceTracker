package com.tracker;

import com.tracker.config.ContainerConfig;
import com.tracker.config.TestSecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Import(ContainerConfig.class)
@SpringBootTest(classes = {TestSecurityConfiguration.class})
class UaaServiceApplicationIT {

    @Test
    void contextLoads() {
        //verify context run
    }

}
