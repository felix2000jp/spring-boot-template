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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@jakarta.persistence.Table(name = "appuser")
@jakarta.persistence.Entity
public class Appuser implements AggregateRoot<Appuser, AppuserId> {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private AppuserId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "username"))
    private Username username;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "password"))
    private Password password;

    @ElementCollection(fetch = FetchType.EAGER)
    @AttributeOverride(name = "value", column = @Column(name = "scope"))
    private List<Scope> scopes;

    @Transient
    private final Collection<DomainEvent> domainEvents = new ArrayList<>();

    protected Appuser() {
    }

    protected Appuser(AppuserId id, Username username, Password password, List<Scope> scopes) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.scopes = scopes;
    }

    public static Appuser from(UUID id, String username, String password, SecurityScope initialScope) {
        return new Appuser(
                new AppuserId(id),
                new Username(username),
                new Password(password),
                List.of(new Scope(initialScope))
        );
    }

    @Override
    public AppuserId getId() {
        return id;
    }

    public Username getUsername() {
        return username;
    }

    public void setUsername(Username username) {
        this.username = username;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public List<Scope> getScopes() {
        return scopes;
    }

    public void delete() {
        var event = new AppuserDeletedEvent(id.value());
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
