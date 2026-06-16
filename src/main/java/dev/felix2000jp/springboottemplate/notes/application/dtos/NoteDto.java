package dev.felix2000jp.springboottemplate.notes.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Note details.")
public record NoteDto(
        @Schema(description = "Note identifier.", example = "42b94c31-ae1c-48bf-aec7-71a58d81f69a")
        UUID id,

        @Schema(description = "Note title.", example = "Project checklist")
        String title,

        @Schema(description = "Note content.", example = "Review CI status before merging.")
        String content
) {
}
