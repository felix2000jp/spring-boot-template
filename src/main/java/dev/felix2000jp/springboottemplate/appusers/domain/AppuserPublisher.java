package dev.felix2000jp.springboottemplate.appusers.domain;

import dev.felix2000jp.springboottemplate.appusers.domain.events.AppuserDeletedEvent;

public interface AppuserPublisher {

    void publish(AppuserDeletedEvent event);

}
