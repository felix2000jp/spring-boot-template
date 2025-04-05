package dev.felix2000jp.springboottemplate.appusers.domain;

public interface AppuserPublisher {

    void publishAppuserCreatedEvent(Appuser appuser);

    void publishAppuserDeletedEvent(Appuser appuser);

}
