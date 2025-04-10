package dev.felix2000jp.springboottemplate.security.domain;

import dev.felix2000jp.springboottemplate.security.SecurityScope;
import dev.felix2000jp.springboottemplate.security.domain.valueobjects.AppuserId;
import dev.felix2000jp.springboottemplate.security.domain.valueobjects.Password;
import dev.felix2000jp.springboottemplate.security.domain.valueobjects.Scope;
import dev.felix2000jp.springboottemplate.security.domain.valueobjects.Username;
import jakarta.persistence.*;
import org.jmolecules.ddd.types.AggregateRoot;

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

    protected Appuser() {
    }

    protected Appuser(AppuserId id, Username username, Password password, List<Scope> scopes) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.scopes = scopes;
    }

    public static Appuser from(UUID id, String username, String password, SecurityScope initialScope) {
        var idValueObject = new AppuserId(id);
        var usernameValueObject = new Username(username);
        var passwordValueObject = new Password(password);
        var scopeValueObject = new Scope(initialScope);

        return new Appuser(
                idValueObject,
                usernameValueObject,
                passwordValueObject,
                List.of(scopeValueObject)
        );
    }

    @Override
    public AppuserId getId() {
        return this.id;
    }

    public Username getUsername() {
        return this.username;
    }

    public void setUsername(Username newUsername) {
        this.username = newUsername;
    }

    public Password getPassword() {
        return this.password;
    }

    public void setPassword(Password newPassword) {
        this.password = newPassword;
    }

    public List<Scope> getScopes() {
        return scopes;
    }

}
