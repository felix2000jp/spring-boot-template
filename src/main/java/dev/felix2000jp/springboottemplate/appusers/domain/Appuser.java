package dev.felix2000jp.springboottemplate.appusers.domain;

import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.AppuserId;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.Password;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.Scope;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.Username;
import dev.felix2000jp.springboottemplate.shared.security.SecurityScope;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.FetchType;
import org.jmolecules.ddd.types.AggregateRoot;

import java.util.List;
import java.util.UUID;

@jakarta.persistence.Table(name = "appuser")
@jakarta.persistence.Entity
public class Appuser implements AggregateRoot<Appuser, AppuserId> {

    @EmbeddedId
    private AppuserId id;

    @Embedded
    private Username username;

    @Embedded
    private Password password;

    @ElementCollection(fetch = FetchType.EAGER)
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
