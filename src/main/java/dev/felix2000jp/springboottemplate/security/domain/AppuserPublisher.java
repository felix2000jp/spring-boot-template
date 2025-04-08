package dev.felix2000jp.springboottemplate.security.domain;

public interface AppuserPublisher {

    void publishAppuserDeletedEvent(Appuser appuser);

}
