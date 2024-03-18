package com.tracker.config.doc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String AUTH_SERVER = "http://localhost:8082/oauth";

    private static final String TITLE = "Auth Service REST API";
    private static final String DESCRIPTION = "Auth Service";
    private static final String LICENSE = "Apache License Version 2.0";
    private static final String LICENSE_URL = "http://www.apache.org/licenses/LICENSE-2.0";
    private static final String VERSION = "1.0.0";

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(TITLE)
                        .description(DESCRIPTION)
                        .version(VERSION)
                        .license(new License().name(LICENSE).url(LICENSE_URL))
                );
    }

}
