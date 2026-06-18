package dev.felix2000jp.springboottemplate.appusers.infrastructure.api;

import dev.felix2000jp.springboottemplate.appusers.application.AppuserService;
import dev.felix2000jp.springboottemplate.appusers.application.dtos.AppuserDto;
import dev.felix2000jp.springboottemplate.appusers.application.dtos.AppuserTokenDto;
import dev.felix2000jp.springboottemplate.appusers.application.dtos.CreateAppuserDto;
import dev.felix2000jp.springboottemplate.appusers.application.dtos.UpdateAppuserDto;
import dev.felix2000jp.springboottemplate.system.openapi.ApiResponseBadRequest;
import dev.felix2000jp.springboottemplate.system.openapi.ApiResponseConflict;
import dev.felix2000jp.springboottemplate.system.openapi.ApiResponseNotFound;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "Appusers", description = "Manage the current app user and authentication token.")
@Validated
@RestController
@RequestMapping("/api/appusers")
class AppuserController {

    private final AppuserService appuserService;

    AppuserController(AppuserService appuserService) {
        this.appuserService = appuserService;
    }

    @Operation(summary = "Get current app user")
    @ApiResponse(responseCode = "200")
    @ApiResponseNotFound
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AppuserDto> get() {
        var body = appuserService.get();
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Create app user")
    @ApiResponse(responseCode = "201")
    @ApiResponseBadRequest
    @ApiResponseConflict
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> create(@Valid @RequestBody CreateAppuserDto createAppuserDto) {
        appuserService.create(createAppuserDto);
        var location = URI.create("/api/appusers");
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Update current app user")
    @ApiResponse(responseCode = "204")
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseConflict
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> update(@Valid @RequestBody UpdateAppuserDto updateAppuserDto) {
        appuserService.update(updateAppuserDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete current app user")
    @ApiResponse(responseCode = "204")
    @ApiResponseNotFound
    @DeleteMapping
    ResponseEntity<Void> delete() {
        appuserService.delete();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create bearer token")
    @ApiResponse(responseCode = "200")
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AppuserTokenDto> login() {
        var body = appuserService.login();
        return ResponseEntity.ok(body);
    }

}
