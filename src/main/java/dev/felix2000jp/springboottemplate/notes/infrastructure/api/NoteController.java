package dev.felix2000jp.springboottemplate.notes.infrastructure.api;

import dev.felix2000jp.springboottemplate.notes.application.NoteService;
import dev.felix2000jp.springboottemplate.notes.application.dtos.CreateNoteDto;
import dev.felix2000jp.springboottemplate.notes.application.dtos.NoteDto;
import dev.felix2000jp.springboottemplate.notes.application.dtos.NoteListDto;
import dev.felix2000jp.springboottemplate.notes.application.dtos.UpdateNoteDto;
import dev.felix2000jp.springboottemplate.system.openapi.ApiResponseBadRequest;
import dev.felix2000jp.springboottemplate.system.openapi.ApiResponseNotFound;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
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
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<NoteListDto> get() {
        var body = noteService.get();
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Get note")
    @ApiResponse(responseCode = "200")
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping(value = "/{noteIdValue}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<NoteDto> getByNoteIdValue(
            @Parameter(description = "Note identifier.", example = "42b94c31-ae1c-48bf-aec7-71a58d81f69a")
            @PathVariable UUID noteIdValue
    ) {
        var body = noteService.getByNoteIdValue(noteIdValue);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Create note")
    @ApiResponse(responseCode = "201")
    @ApiResponseBadRequest
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> create(@RequestBody @Valid CreateNoteDto createNoteDto) {
        noteService.create(createNoteDto);
        var location = URI.create("/api/notes");
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Update note")
    @ApiResponse(responseCode = "204")
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @PutMapping(value = "/{noteIdValue}", consumes = MediaType.APPLICATION_JSON_VALUE)
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
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @DeleteMapping("/{noteIdValue}")
    ResponseEntity<Void> deleteByNoteIdValue(
            @Parameter(description = "Note identifier.", example = "42b94c31-ae1c-48bf-aec7-71a58d81f69a")
            @PathVariable UUID noteIdValue
    ) {
        noteService.deleteByNoteIdValue(noteIdValue);
        return ResponseEntity.noContent().build();
    }

}
