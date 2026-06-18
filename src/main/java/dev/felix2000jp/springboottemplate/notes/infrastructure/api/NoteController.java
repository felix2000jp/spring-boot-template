package dev.felix2000jp.springboottemplate.notes.infrastructure.api;

import dev.felix2000jp.springboottemplate.notes.application.NoteService;
import dev.felix2000jp.springboottemplate.notes.application.dtos.CreateNoteDto;
import dev.felix2000jp.springboottemplate.notes.application.dtos.NoteDto;
import dev.felix2000jp.springboottemplate.notes.application.dtos.NoteListDto;
import dev.felix2000jp.springboottemplate.notes.application.dtos.UpdateNoteDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@Tag(name = "Notes", description = "Manage notes owned by the current app user.")
@Validated
@RestController
@RequestMapping("/api/notes")
class NoteController {

    private final NoteService noteService;

    NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @Operation(summary = "List notes")
    @ApiResponse(responseCode = "200")
    @GetMapping
    ResponseEntity<NoteListDto> get() {
        var body = noteService.get();
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Get note")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "404")
    @GetMapping(value = "/{noteIdValue}")
    ResponseEntity<NoteDto> getByNoteIdValue(
            @Parameter(description = "Note identifier.", example = "42b94c31-ae1c-48bf-aec7-71a58d81f69a")
            @PathVariable UUID noteIdValue
    ) {
        var body = noteService.getByNoteIdValue(noteIdValue);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Create note")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "400")
    @PostMapping
    ResponseEntity<Void> create(@RequestBody @Valid CreateNoteDto createNoteDto) {
        noteService.create(createNoteDto);
        var location = URI.create("/api/notes");
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Update note")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "404")
    @PutMapping(value = "/{noteIdValue}")
    ResponseEntity<Void> update(
            @Parameter(description = "Note identifier.", example = "42b94c31-ae1c-48bf-aec7-71a58d81f69a")
            @PathVariable UUID noteIdValue,
            @RequestBody @Valid UpdateNoteDto updateNoteDto
    ) {
        noteService.update(noteIdValue, updateNoteDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete note")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "404")
    @DeleteMapping("/{noteIdValue}")
    ResponseEntity<Void> deleteByNoteIdValue(
            @Parameter(description = "Note identifier.", example = "42b94c31-ae1c-48bf-aec7-71a58d81f69a")
            @PathVariable UUID noteIdValue
    ) {
        noteService.deleteByNoteIdValue(noteIdValue);
        return ResponseEntity.noContent().build();
    }

}
