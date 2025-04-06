package dev.felix2000jp.springboottemplate.appusers.infrastructure.queue;

import dev.felix2000jp.springboottemplate.TestcontainersConfiguration;
import dev.felix2000jp.springboottemplate.appusers.domain.Appuser;
import dev.felix2000jp.springboottemplate.appusers.domain.AppuserPublisher;
import dev.felix2000jp.springboottemplate.appusers.domain.events.AppuserDeletedEvent;
import dev.felix2000jp.springboottemplate.shared.security.SecurityScope;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ApplicationModuleTest
@Import({TestcontainersConfiguration.class})
class DefaultAppuserPublisherIntegrationTest {

    @Autowired
    private AppuserPublisher eventPublisher;

    @Test
    void publishAppuserDeletedEvent_given_appuser_then_publish_appuser_deleted_event(Scenario scenario) {
        var appuser = Appuser.from(UUID.randomUUID(), "username", "password", SecurityScope.APPLICATION);

        scenario
                .stimulate(() -> eventPublisher.publishAppuserDeletedEvent(appuser))
                .andWaitForEventOfType(AppuserDeletedEvent.class)
                .toArriveAndVerify(event -> assertThat(event.appuserId()).isEqualTo(appuser.getId().value()));
    }

}