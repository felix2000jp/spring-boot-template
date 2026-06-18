package dev.felix2000jp.springboottemplate.system.configurations;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

@Configuration
class OpenApiConfiguration {

    private static final String BASIC_AUTH_SECURITY_SCHEME = "basic-auth";
    private static final String BEARER_AUTH_SECURITY_SCHEME = "bearer-auth";
    private static final String PROBLEM_DETAIL_SCHEMA = "ProblemDetail";

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
                                .addSchemas(PROBLEM_DETAIL_SCHEMA, problemDetailSchema())
                                .addResponses("BadRequest", problemDetailResponse("Bad Request"))
                                .addResponses("NotFound", problemDetailResponse("Not Found"))
                                .addResponses("Conflict", problemDetailResponse("Conflict"))
                );
    }

    private static Schema<?> problemDetailSchema() {
        return new Schema<>()
                .type("object")
                .addProperty("type", new StringSchema().format("uri"))
                .addProperty("title", new StringSchema())
                .addProperty("status", new IntegerSchema().format("int32"))
                .addProperty("detail", new StringSchema())
                .addProperty("instance", new StringSchema().format("uri"));
    }

    private static ApiResponse problemDetailResponse(String description) {
        return new ApiResponse().description(description).content(
                new Content().addMediaType(
                        MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().schema(
                                new Schema<>().$ref("#/components/schemas/" + PROBLEM_DETAIL_SCHEMA)
                        )
                )
        );
    }
}
