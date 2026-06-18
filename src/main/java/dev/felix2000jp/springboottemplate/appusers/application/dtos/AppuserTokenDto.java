package dev.felix2000jp.springboottemplate.appusers.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication token response.")
public record AppuserTokenDto(
        @Schema(description = "JWT bearer token.", example = "eyJhbGciOiJSUzI1NiJ9...")
        String token
) {
}
