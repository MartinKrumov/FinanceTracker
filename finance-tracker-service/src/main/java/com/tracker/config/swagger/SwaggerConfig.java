package com.tracker.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final String BASE_PACKAGES = "com.tracker.rest";

    private static final String TITLE = "Finance Tracker Service REST API";
    private static final String DESCRIPTION = "Finance Tracker Service";
    private static final String LICENSE = "Apache License Version 2.0";
    private static final String LICENSE_URL = "http://www.apache.org/licenses/LICENSE-2.0";
    private static final String VERSION = "1.0.0";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGES))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    /**
     * @return API info for swagger-ui.html
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(TITLE)
                .description(DESCRIPTION)
                .license(LICENSE)
                .licenseUrl(LICENSE_URL)
                .version(VERSION)
                .build();
    }
}
