package dev.felix2000jp.springboottemplate.appusers.domain;

import dev.felix2000jp.springboottemplate.appusers.domain.events.AppuserDeletedEvent;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.AppuserId;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.Password;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.Scope;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.Username;
import dev.felix2000jp.springboottemplate.system.security.SecurityScope;
import jakarta.persistence.*;
import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.event.types.DomainEvent;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import java.util.*;

@jakarta.persistence.Table(name = "appuser")
@jakarta.persistence.Entity
public class Appuser implements AggregateRoot<Appuser, AppuserId> {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "scope")
    private Collection<SecurityScope> scopes;

    @Transient
    private final Collection<DomainEvent> domainEvents = new ArrayList<>();

    protected Appuser() {
    }

    protected Appuser(UUID id, String username, String password, List<SecurityScope> scopes) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.scopes = scopes;
    }

    public static Appuser from(AppuserId id, Username username, Password password, Scope initialScope) {
        return new Appuser(id.value(), username.value(), password.value(), List.of(initialScope.value())
        );
    }

    @Override
    public AppuserId getId() {
        return new AppuserId(id);
    }

    public Username getUsername() {
        return new Username(username);
    }

    public void setUsername(Username username) {
        this.username = username.value();
    }

    public Password getPassword() {
        return new Password(password);
    }

    public void setPassword(Password password) {
        this.password = password.value();
    }

    public List<Scope> getScopes() {
        return scopes.stream().map(Scope::new).toList();
    }

    public void delete() {
        var event = new AppuserDeletedEvent(getId().value());
        domainEvents.add(event);
    }

    @DomainEvents
    Collection<DomainEvent> getDomainEvents() {
        return domainEvents;
    }

    @AfterDomainEventPublication
    void clearDomainEvents() {
        domainEvents.clear();
    }

}
