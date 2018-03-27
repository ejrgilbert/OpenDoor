package com.opendoor.documentation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

// source: https://springframework.guru/spring-boot-restful-api-documentation-with-swagger-2/

// to see json: http://localhost:8080/v2/api-docs
// *** main site to use: http://localhost:8080/swagger-ui.html ***
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket openDoorApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.opendoor.controller"))
                .paths(regex("/.*"))
                .build();

    }
}
