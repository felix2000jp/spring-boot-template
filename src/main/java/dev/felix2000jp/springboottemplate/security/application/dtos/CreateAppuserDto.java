package dev.felix2000jp.springboottemplate.security.application.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAppuserDto(
        @NotBlank
        @Size(min = 3, max = 255)
        String username,

        @NotBlank
        @Size(min = 8, max = 255)
        String password
) {
}
