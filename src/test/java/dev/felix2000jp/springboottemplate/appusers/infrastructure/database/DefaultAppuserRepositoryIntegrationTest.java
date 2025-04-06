package dev.felix2000jp.springboottemplate.appusers.infrastructure.database;

import dev.felix2000jp.springboottemplate.TestcontainersConfiguration;
import dev.felix2000jp.springboottemplate.appusers.domain.Appuser;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.AppuserId;
import dev.felix2000jp.springboottemplate.shared.security.SecurityScope;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({TestcontainersConfiguration.class, DefaultAppuserRepository.class})
class DefaultAppuserRepositoryIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DefaultAppuserRepository appuserRepository;

    private Appuser appuser;

    @BeforeEach
    void setUp() {
        TestcontainersConfiguration.truncateAppuserTables(jdbcTemplate);

        appuser = Appuser.from(UUID.randomUUID(), "username", "password", SecurityScope.APPLICATION);
        appuserRepository.save(appuser);
    }

    @Test
    void findById_given_id_of_appuser_then_return_appuser() {
        var idValueObject = appuser.getId();
        var actual = appuserRepository.findById(idValueObject);

        assertThat(actual).isPresent();
    }

    @Test
    void findById_given_not_found_id_then_return_empty_optional() {
        var idValueObject = new AppuserId(UUID.randomUUID());
        var actual = appuserRepository.findById(idValueObject);

        assertThat(actual).isNotPresent();
    }
}
