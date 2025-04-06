package dev.felix2000jp.springboottemplate.appusers.domain;

public interface AppuserPublisher {

    void publishAppuserDeletedEvent(Appuser appuser);

}
