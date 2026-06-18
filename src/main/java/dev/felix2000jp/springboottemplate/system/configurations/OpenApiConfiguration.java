package dev.felix2000jp.springboottemplate.system.configurations;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpenApiConfiguration {

    private static final String BASIC_AUTH_SECURITY_SCHEME = "basic-auth";
    private static final String BEARER_AUTH_SECURITY_SCHEME = "bearer-auth";

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Spring Boot Template")
                                .description("A ready to use spring boot template")
                                .version("0.0.1-SNAPSHOT")
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        BASIC_AUTH_SECURITY_SCHEME,
                                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")
                                )
                                .addSecuritySchemes(
                                        BEARER_AUTH_SECURITY_SCHEME,
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }

}
