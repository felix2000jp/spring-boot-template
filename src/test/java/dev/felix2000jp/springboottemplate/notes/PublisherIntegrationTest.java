package dev.felix2000jp.springboottemplate.notes;

import dev.felix2000jp.springboottemplate.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;

@ApplicationModuleTest
@Import({TestcontainersConfiguration.class})
class PublisherIntegrationTest {

    @Test
    void publishAppuserDeletedEvent_given_appuser_then_publish_appuser_deleted_event(Scenario scenario) {
    }

}