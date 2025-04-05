package dev.felix2000jp.springboottemplate.security.application.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record AppuserDto(
        @NotNull
        UUID id,

        @Size(min = 3, max = 255)
        String username,

        @NotNull
        List<String> scopes
) {
}
