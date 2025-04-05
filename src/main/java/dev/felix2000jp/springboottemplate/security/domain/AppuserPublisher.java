package dev.felix2000jp.springboottemplate.security.domain;

public interface AppuserPublisher {

    void publishAppuserCreatedEvent(Appuser appuser);

    void publishAppuserDeletedEvent(Appuser appuser);

}
