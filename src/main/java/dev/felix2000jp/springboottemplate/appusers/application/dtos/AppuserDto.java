package dev.felix2000jp.springboottemplate.appusers.application.dtos;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(description = "App user details.")
public record AppuserDto(
        @Schema(description = "App user identifier.", example = "2b6fa8bc-6f94-41f6-b5e6-9915d0c3d4d3")
        UUID id,

        @Schema(description = "Unique username.", example = "joao")
        String username,

        @ArraySchema(schema = @Schema(description = "Granted application scope.", example = "APPLICATION"))
        List<String> scopes
) {
}
