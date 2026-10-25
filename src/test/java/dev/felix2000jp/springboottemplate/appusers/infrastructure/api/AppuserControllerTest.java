package dev.felix2000jp.springboottemplate.appusers.infrastructure.api;

import dev.felix2000jp.springboottemplate.appusers.application.AppuserService;
import dev.felix2000jp.springboottemplate.appusers.application.dtos.AppuserDto;
import dev.felix2000jp.springboottemplate.appusers.application.dtos.AppuserTokenDto;
import dev.felix2000jp.springboottemplate.appusers.application.dtos.CreateAppuserDto;
import dev.felix2000jp.springboottemplate.appusers.application.dtos.UpdateAppuserDto;
import dev.felix2000jp.springboottemplate.appusers.domain.exceptions.AppuserAlreadyExistsException;
import dev.felix2000jp.springboottemplate.appusers.domain.exceptions.AppuserNotFoundException;
import dev.felix2000jp.springboottemplate.system.configurations.TestSecurityConfiguration;
import dev.felix2000jp.springboottemplate.system.security.SecurityScope;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AppuserController.class)
@Import(TestSecurityConfiguration.class)
class AppuserControllerTest {

    @MockitoBean
    private AppuserService appuserService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JsonMapper jsonMapper;

    @Test
    @WithMockUser(authorities = SecurityScope.MEMBER_AUTHORITY)
    void get_given_unexpected_exception_then_return_generic_problem_detail() throws Exception {
        when(appuserService.get()).thenThrow(new IllegalStateException("Internal implementation detail"));

        mockMvc.perform(get("/api/appusers"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Internal Server Error"))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.detail").value("An error occurred while processing the request"))
                .andExpect(jsonPath("$.instance").value("/api/appusers"));
    }

    @Test
    void register_given_malformed_json_then_return_spring_problem_detail() throws Exception {
        mockMvc.perform(post("/api/appusers").contentType(MediaType.APPLICATION_JSON).content("{"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.instance").value("/api/appusers"));

        verifyNoInteractions(appuserService);
    }

    @Test
    @WithMockUser(authorities = SecurityScope.MEMBER_AUTHORITY)
    void get_then_return_200_and_appuser() throws Exception {
        var appuserDto = new AppuserDto(UUID.randomUUID(), "username", List.of(SecurityScope.MEMBER.name()));
        var expectedResponse = jsonMapper.writeValueAsString(appuserDto);

        when(appuserService.get()).thenReturn(appuserDto);

        var request = get("/api/appusers");
        mockMvc
                .perform(request.with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    @WithMockUser(authorities = SecurityScope.MEMBER_AUTHORITY)
    void get_given_not_found_security_user_then_return_404() throws Exception {
        var exception = new AppuserNotFoundException();
        when(appuserService.get()).thenThrow(exception);

        var request = get("/api/appusers");
        mockMvc
                .perform(request.with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Not Found"))
                .andExpect(jsonPath("$.detail").value(exception.getMessage()))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @WithMockUser(authorities = SecurityScope.MEMBER_AUTHORITY)
    void update_then_return_204() throws Exception {
        var updateAppuserDto = new UpdateAppuserDto("new username", "new password");

        var body = jsonMapper.writeValueAsString(updateAppuserDto);

        var request = put("/api/appusers").contentType(MediaType.APPLICATION_JSON).content(body);
        mockMvc
                .perform(request.with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = SecurityScope.MEMBER_AUTHORITY)
    void update_given_duplicate_username_then_return_409() throws Exception {
        var updateAppuserDto = new UpdateAppuserDto("new username", "new password");

        var body = jsonMapper.writeValueAsString(updateAppuserDto);

        var exception = new AppuserAlreadyExistsException();
        doThrow(exception).when(appuserService).update(updateAppuserDto);

        var request = put("/api/appusers").contentType(MediaType.APPLICATION_JSON).content(body);
        mockMvc
                .perform(request.with(csrf()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Conflict"))
                .andExpect(jsonPath("$.detail").value(exception.getMessage()))
                .andExpect(jsonPath("$.status").value(409));
    }

    @ParameterizedTest
    @MethodSource
    @WithMockUser(authorities = SecurityScope.MEMBER_AUTHORITY)
    void update_given_invalid_body_then_return_400(String username, String password) throws Exception {
        var updateAppuserDto = new UpdateAppuserDto(username, password);
        var body = jsonMapper.writeValueAsString(updateAppuserDto);
        var request = put("/api/appusers").contentType(MediaType.APPLICATION_JSON).content(body);
        mockMvc
                .perform(request.with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400));
    }

    private static Stream<Arguments> update_given_invalid_body_then_return_400() {
        return Stream.of(
                arguments(null, "password"),
                arguments("", "password"),
                arguments(" ", "password"),
                arguments("a", "password"),
                arguments("a".repeat(256), "password"),
                arguments("username", null),
                arguments("username", ""),
                arguments("username", " "),
                arguments("username", "a"),
                arguments("username", "a".repeat(256))
        );
    }

    @Test
    @WithMockUser(authorities = SecurityScope.MEMBER_AUTHORITY)
    void delete_then_return_204() throws Exception {
        var request = delete("/api/appusers");
        mockMvc
                .perform(request.with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = SecurityScope.MEMBER_AUTHORITY)
    void delete_given_not_found_security_user_then_return_404() throws Exception {
        var exception = new AppuserNotFoundException();
        doThrow(exception).when(appuserService).delete();

        var request = delete("/api/appusers");
        mockMvc
                .perform(request.with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Not Found"))
                .andExpect(jsonPath("$.detail").value(exception.getMessage()))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void register_then_return_201_and_location_header() throws Exception {
        var createAppuserDto = new CreateAppuserDto("username", "password");

        var body = jsonMapper.writeValueAsString(createAppuserDto);

        var request = post("/api/appusers").contentType(MediaType.APPLICATION_JSON).content(body);
        mockMvc
                .perform(request.with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(header().string("LOCATION", "/api/appusers"));
    }

    @Test
    void register_given_duplicate_username_then_return_409() throws Exception {
        var createAppuserDto = new CreateAppuserDto("username", "password");

        var body = jsonMapper.writeValueAsString(createAppuserDto);

        var exception = new AppuserAlreadyExistsException();
        doThrow(exception).when(appuserService).register(createAppuserDto);

        var request = post("/api/appusers").contentType(MediaType.APPLICATION_JSON).content(body);
        mockMvc
                .perform(request.with(csrf()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Conflict"))
                .andExpect(jsonPath("$.detail").value(exception.getMessage()))
                .andExpect(jsonPath("$.status").value(409));
    }

    @ParameterizedTest
    @MethodSource
    void register_given_invalid_body_then_return_400(String username, String password) throws Exception {
        var createAppuserDto = new CreateAppuserDto(username, password);
        var body = jsonMapper.writeValueAsString(createAppuserDto);
        var request = post("/api/appusers").contentType(MediaType.APPLICATION_JSON).content(body);
        mockMvc
                .perform(request.with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400));
    }

    private static Stream<Arguments> register_given_invalid_body_then_return_400() {
        return Stream.of(
                arguments(null, "password"),
                arguments("", "password"),
                arguments(" ", "password"),
                arguments("a", "password"),
                arguments("a".repeat(256), "password"),
                arguments("username", null),
                arguments("username", ""),
                arguments("username", " "),
                arguments("username", "a"),
                arguments("username", "a".repeat(256))
        );
    }

    @Test
    @WithMockUser
    void login_then_return_200_and_token() throws Exception {
        var appuserTokenDto = new AppuserTokenDto("sample-jwt-token");
        var expectedResponse = jsonMapper.writeValueAsString(appuserTokenDto);

        when(appuserService.login()).thenReturn(appuserTokenDto);

        var request = post("/api/appusers/login");
        mockMvc
                .perform(request.with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void login_given_bearer_token_then_return_403() throws Exception {
        mockMvc
                .perform(post("/api/appusers/login").with(jwt().authorities(SecurityScope.MEMBER)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(appuserService);
    }

    @Test
    void login_given_anonymous_user_then_return_401() throws Exception {
        mockMvc.perform(post("/api/appusers/login")).andExpect(status().isUnauthorized());

        verifyNoInteractions(appuserService);
    }

    @ParameterizedTest
    @CsvSource({
            "GET, /api/appusers",
            "PUT, /api/appusers",
            "DELETE, /api/appusers"
    })
    void protected_endpoint_given_anonymous_user_then_return_401(String method, String uri) throws Exception {
        var request = request(HttpMethod.valueOf(method), uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"username": "username", "password": "password"}
                        """);

        mockMvc.perform(request).andExpect(status().isUnauthorized());

        verifyNoInteractions(appuserService);
    }

    @ParameterizedTest
    @CsvSource({
            "GET, /api/appusers",
            "PUT, /api/appusers",
            "DELETE, /api/appusers"
    })
    @WithMockUser(authorities = SecurityScope.ADMIN_AUTHORITY)
    void protected_endpoint_given_user_without_member_scope_then_return_403(String method, String uri) throws Exception {
        var request = request(HttpMethod.valueOf(method), uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"username": "username", "password": "password"}
                        """);

        mockMvc.perform(request).andExpect(status().isForbidden());

        verifyNoInteractions(appuserService);
    }

}
