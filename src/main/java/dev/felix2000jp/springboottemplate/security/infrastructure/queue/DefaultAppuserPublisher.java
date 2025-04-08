package dev.felix2000jp.springboottemplate.security.infrastructure.queue;

import dev.felix2000jp.springboottemplate.security.domain.Appuser;
import dev.felix2000jp.springboottemplate.security.domain.AppuserPublisher;
import dev.felix2000jp.springboottemplate.security.domain.events.AppuserDeletedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
class DefaultAppuserPublisher implements AppuserPublisher {

    private final ApplicationEventPublisher events;

    DefaultAppuserPublisher(ApplicationEventPublisher events) {
        this.events = events;
    }

    @Override
    public void publishAppuserDeletedEvent(Appuser appuser) {
        var id = appuser.getId().value();
        var event = new AppuserDeletedEvent(id);

        events.publishEvent(event);
    }

}
