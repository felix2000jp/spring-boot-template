package dev.felix2000jp.springboottemplate.notes.application.dtos;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Pageless note list response.")
public record NoteListDto(
        @Schema(description = "Total number of notes returned.", example = "1")
        int total,

        @ArraySchema(schema = @Schema(implementation = NoteDto.class))
        List<NoteDto> notes
) {
}
