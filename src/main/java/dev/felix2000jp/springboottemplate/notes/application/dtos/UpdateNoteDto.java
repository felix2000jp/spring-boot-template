package dev.felix2000jp.springboottemplate.notes.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request body for updating a note.")
public record UpdateNoteDto(
        @Schema(description = "Updated note title.", example = "Updated project checklist")
        @NotBlank
        String title,

        @Schema(description = "Updated note content.", example = "Review tests and documentation before merging.")
        @NotBlank
        String content
) {
}
