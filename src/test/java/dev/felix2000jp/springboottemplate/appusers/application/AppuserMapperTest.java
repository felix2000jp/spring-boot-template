package dev.felix2000jp.springboottemplate.appusers.application;

import dev.felix2000jp.springboottemplate.appusers.domain.Appuser;
import dev.felix2000jp.springboottemplate.shared.security.SecurityScope;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AppuserMapperTest {

    private final AppuserMapper appuserMapper = new AppuserMapper();

    @Test
    void toDto_given_appuser_then_map_to_dto() {
        var appuser = Appuser.from(UUID.randomUUID(), "username", "password", SecurityScope.APPLICATION);

        var actual = appuserMapper.toDto(appuser);

        assertThat(actual.id()).isEqualTo(appuser.getId().value());
        assertThat(actual.username()).isEqualTo(appuser.getUsername().value());
        assertThat(actual.scopes()).contains(SecurityScope.APPLICATION.name());
    }

}
