package dev.felix2000jp.springboottemplate.appusers.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for registering a new app user.")
public record CreateAppuserDto(
        @Schema(description = "Unique username used to identify the app user.", example = "joao")
        @NotBlank
        @Size(min = 3, max = 255)
        String username,

        @Schema(description = "Password used for Basic authentication.", example = "correct-horse-battery-staple")
        @NotBlank
        @Size(min = 8, max = 255)
        String password
) {
}
