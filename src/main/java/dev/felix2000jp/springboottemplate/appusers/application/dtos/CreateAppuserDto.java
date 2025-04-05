package dev.felix2000jp.springboottemplate.appusers.application.dtos;

import jakarta.validation.constraints.Size;

public record CreateAppuserDto(
        @Size(min = 3, max = 255)
        String username,

        @Size(min = 8, max = 255)
        String password
) {
}
