package com.coupon.manager.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI couponAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Coupon Manager")
                        .description("Coupon Manager Application")
                        .version("v0.0.1")
                        .license(new License()
                                .name("Apache 1.0")
                                .url("https://github.com/NogueiraLn/coupon-manager")));
    }
}