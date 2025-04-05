package dev.felix2000jp.springboottemplate.appusers.infrastructure.queue;

import dev.felix2000jp.springboottemplate.appusers.domain.Appuser;
import dev.felix2000jp.springboottemplate.appusers.domain.AppuserPublisher;
import dev.felix2000jp.springboottemplate.appusers.domain.events.AppuserCreatedEvent;
import dev.felix2000jp.springboottemplate.appusers.domain.events.AppuserDeletedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
class DefaultAppuserPublisher implements AppuserPublisher {

    private final ApplicationEventPublisher events;

    DefaultAppuserPublisher(ApplicationEventPublisher events) {
        this.events = events;
    }

    @Override
    public void publishAppuserCreatedEvent(Appuser appuser) {
        var id = appuser.getId().value();
        var event = new AppuserCreatedEvent(id);

        events.publishEvent(event);
    }

    @Override
    public void publishAppuserDeletedEvent(Appuser appuser) {
        var id = appuser.getId().value();
        var event = new AppuserDeletedEvent(id);

        events.publishEvent(event);
    }

}
