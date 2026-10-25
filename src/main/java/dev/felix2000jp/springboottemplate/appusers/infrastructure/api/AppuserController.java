package dev.felix2000jp.springboottemplate.appusers.infrastructure.api;

import dev.felix2000jp.springboottemplate.appusers.application.AppuserService;
import dev.felix2000jp.springboottemplate.appusers.application.dtos.AppuserDto;
import dev.felix2000jp.springboottemplate.appusers.application.dtos.AppuserTokenDto;
import dev.felix2000jp.springboottemplate.appusers.application.dtos.CreateAppuserDto;
import dev.felix2000jp.springboottemplate.appusers.application.dtos.UpdateAppuserDto;
import dev.felix2000jp.springboottemplate.system.security.SecurityScope;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @ApiResponse(responseCode = "200", description = "Current app user returned.")
    @PreAuthorize("hasAuthority('" + SecurityScope.MEMBER_AUTHORITY + "')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AppuserDto> get() {
        var body = appuserService.get();
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Update current app user")
    @ApiResponse(responseCode = "204", description = "Current app user updated.")
    @PreAuthorize("hasAuthority('" + SecurityScope.MEMBER_AUTHORITY + "')")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> update(@Valid @RequestBody UpdateAppuserDto updateAppuserDto) {
        appuserService.update(updateAppuserDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete current app user")
    @ApiResponse(responseCode = "204", description = "Current app user deleted.")
    @PreAuthorize("hasAuthority('" + SecurityScope.MEMBER_AUTHORITY + "')")
    @DeleteMapping
    ResponseEntity<Void> delete() {
        appuserService.delete();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Register app user")
    @ApiResponse(responseCode = "201", description = "App user created.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> register(@Valid @RequestBody CreateAppuserDto createAppuserDto) {
        appuserService.register(createAppuserDto);
        var location = URI.create("/api/appusers");
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Create bearer token")
    @ApiResponse(responseCode = "200", description = "Bearer token created.")
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AppuserTokenDto> login() {
        var body = appuserService.login();
        return ResponseEntity.ok(body);
    }

}
