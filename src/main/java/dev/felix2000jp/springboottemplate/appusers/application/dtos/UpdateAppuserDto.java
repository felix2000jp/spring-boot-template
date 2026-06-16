package dev.felix2000jp.springboottemplate.appusers.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for updating the current app user.")
public record UpdateAppuserDto(
        @Schema(description = "New unique username for the app user.", example = "joao.updated")
        @NotBlank
        @Size(min = 3, max = 255)
        String username,

        @Schema(description = "New password used for Basic authentication.", example = "new-correct-horse-battery-staple")
        @NotBlank
        @Size(min = 8, max = 255)
        String password
) {
}
