package dev.felix2000jp.springboottemplate.security.application;

import dev.felix2000jp.springboottemplate.security.application.dtos.CreateAppuserDto;
import dev.felix2000jp.springboottemplate.security.application.dtos.UpdateAppuserDto;
import dev.felix2000jp.springboottemplate.security.domain.Appuser;
import dev.felix2000jp.springboottemplate.security.domain.AppuserPublisher;
import dev.felix2000jp.springboottemplate.security.domain.AppuserRepository;
import dev.felix2000jp.springboottemplate.security.domain.exceptions.AppuserAlreadyExistsException;
import dev.felix2000jp.springboottemplate.security.domain.exceptions.AppuserNotFoundException;
import dev.felix2000jp.springboottemplate.security.domain.valueobjects.AppuserId;
import dev.felix2000jp.springboottemplate.security.domain.valueobjects.Username;
import dev.felix2000jp.springboottemplate.security.SecurityScope;
import dev.felix2000jp.springboottemplate.security.SecurityService;
import dev.felix2000jp.springboottemplate.security.SecurityUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppuserServiceTest {

    @Mock
    private AppuserRepository appuserRepository;
    @Mock
    private AppuserPublisher appuserPublisher;
    @Spy
    private AppuserMapper appuserMapper;
    @Mock
    private SecurityService securityService;
    @InjectMocks
    private AppuserService appuserService;

    @Captor
    private ArgumentCaptor<Appuser> appuserCaptor;

    @Test
    void get_then_return_appuser_dto() {
        var securityUser = new SecurityUser(
                UUID.randomUUID(),
                "username",
                "password",
                SecurityScope.APPLICATION
        );
        var appuser = Appuser.from(
                securityUser.getId(),
                securityUser.getUsername(),
                securityUser.getPassword(),
                SecurityScope.APPLICATION
        );

        when(securityService.loadUserFromSecurityContext()).thenReturn(securityUser);
        when(appuserRepository.findById(appuser.getId())).thenReturn(Optional.of(appuser));

        var actual = appuserService.get();

        assertThat(actual.id()).isEqualTo(appuser.getId().value());
        assertThat(actual.username()).isEqualTo(appuser.getUsername().value());
    }

    @Test
    void get_given_not_found_security_user_then_throw_exception() {
        var securityUser = new SecurityUser(
                UUID.randomUUID(),
                "username",
                "password",
                SecurityScope.APPLICATION
        );

        when(securityService.loadUserFromSecurityContext()).thenReturn(securityUser);
        when(appuserRepository.findById(new AppuserId(securityUser.getId()))).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                appuserService.get()
        ).isInstanceOf(AppuserNotFoundException.class);
    }

    @Test
    void create_given_dto_then_create_appuser() {
        var createAppuserDto = new CreateAppuserDto("username", "password");

        when(appuserRepository.existsByUsername(new Username(createAppuserDto.username()))).thenReturn(false);
        when(securityService.generateEncodedPassword(createAppuserDto.password())).thenReturn("encoded-password");

        appuserService.create(createAppuserDto);

        verify(appuserRepository).save(appuserCaptor.capture());

        assertThat(appuserCaptor.getValue().getUsername().value()).isEqualTo(createAppuserDto.username());
        assertThat(appuserCaptor.getValue().getPassword().value()).isEqualTo("encoded-password");
    }

    @Test
    void create_given_dto_with_duplicate_username_then_throw_exception() {
        var createAppuserDto = new CreateAppuserDto("username", "password");

        when(appuserRepository.existsByUsername(new Username(createAppuserDto.username()))).thenReturn(true);
        when(securityService.generateEncodedPassword(createAppuserDto.password())).thenReturn("encoded-password");

        assertThatThrownBy(() ->
                appuserService.create(createAppuserDto)
        ).isInstanceOf(AppuserAlreadyExistsException.class);
    }

    @Test
    void update_given_dto_then_update_username_and_password() {
        var securityUser = new SecurityUser(
                UUID.randomUUID(),
                "username",
                "password",
                SecurityScope.APPLICATION
        );
        var appuser = Appuser.from(
                securityUser.getId(),
                securityUser.getUsername(),
                securityUser.getPassword(),
                SecurityScope.APPLICATION
        );
        var updateAppuserDto = new UpdateAppuserDto("new username", "new password");

        when(securityService.loadUserFromSecurityContext()).thenReturn(securityUser);
        when(appuserRepository.findById(appuser.getId())).thenReturn(Optional.of(appuser));
        when(appuserRepository.existsByUsername(new Username(updateAppuserDto.username()))).thenReturn(false);
        when(securityService.generateEncodedPassword(updateAppuserDto.password())).thenReturn("encoded-password");

        appuserService.update(updateAppuserDto);

        verify(appuserRepository).save(appuserCaptor.capture());
        assertThat(appuserCaptor.getValue().getUsername().value()).isEqualTo("new username");
        assertThat(appuserCaptor.getValue().getPassword().value()).isEqualTo("encoded-password");
    }

    @Test
    void update_given_dto_with_duplicate_username_then_throw_exception() {
        var securityUser = new SecurityUser(
                UUID.randomUUID(),
                "username",
                "password",
                SecurityScope.APPLICATION
        );
        var appuser = Appuser.from(
                securityUser.getId(),
                securityUser.getUsername(),
                securityUser.getPassword(),
                SecurityScope.APPLICATION
        );
        var updateAppuserDto = new UpdateAppuserDto("new username", "new password");

        when(securityService.loadUserFromSecurityContext()).thenReturn(securityUser);
        when(appuserRepository.findById(appuser.getId())).thenReturn(Optional.of(appuser));
        when(appuserRepository.existsByUsername(new Username(updateAppuserDto.username()))).thenReturn(true);
        when(securityService.generateEncodedPassword(updateAppuserDto.password())).thenReturn("encoded-password");

        assertThatThrownBy(() ->
                appuserService.update(updateAppuserDto)
        ).isInstanceOf(AppuserAlreadyExistsException.class);
    }

    @Test
    void delete_then_delete_appuser() {
        var securityUser = new SecurityUser(
                UUID.randomUUID(),
                "username",
                "password",
                SecurityScope.APPLICATION
        );
        var appuser = Appuser.from(
                securityUser.getId(),
                securityUser.getUsername(),
                securityUser.getPassword(),
                SecurityScope.APPLICATION
        );

        when(securityService.loadUserFromSecurityContext()).thenReturn(securityUser);
        when(appuserRepository.findById(appuser.getId())).thenReturn(Optional.of(appuser));

        appuserService.delete();

        verify(appuserRepository).delete(appuser);
        verify(appuserPublisher).publishAppuserDeletedEvent(appuser);
    }

    @Test
    void delete_given_not_found_security_user_then_throw_exception() {
        var securityUser = new SecurityUser(
                UUID.randomUUID(),
                "username",
                "password",
                SecurityScope.APPLICATION
        );

        when(securityService.loadUserFromSecurityContext()).thenReturn(securityUser);
        when(appuserRepository.findById(new AppuserId(securityUser.getId()))).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                appuserService.delete()
        ).isInstanceOf(AppuserNotFoundException.class);
    }
}