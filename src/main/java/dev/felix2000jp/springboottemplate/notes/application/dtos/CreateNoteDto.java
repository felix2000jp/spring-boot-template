package dev.felix2000jp.springboottemplate.notes.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request body for creating a note.")
public record CreateNoteDto(
        @Schema(description = "Note title.", example = "Project checklist")
        @NotBlank
        String title,

        @Schema(description = "Note content.", example = "Review CI status before merging.")
        @NotBlank
        String content
) {
}
