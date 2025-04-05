package dev.felix2000jp.springboottemplate.appusers.domain;

import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.AppuserId;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.Password;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.Scopes;
import dev.felix2000jp.springboottemplate.appusers.domain.valueobjects.Username;
import dev.felix2000jp.springboottemplate.shared.security.SecurityRole;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
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

    @Embedded
    private Scopes scopes;

    protected Appuser() {
    }

    protected Appuser(AppuserId id, Username username, Password password, Scopes scopes) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.scopes = scopes;
    }

    public static Appuser from(UUID id, String username, String password, List<SecurityRole> scopes) {
        var idObject = new AppuserId(id);
        var usernameObject = new Username(username);
        var passwordObject = new Password(password);
        var scopesObject = new Scopes(scopes);

        return new Appuser(idObject, usernameObject, passwordObject, scopesObject);
    }

    @Override
    public AppuserId getId() {
        return this.id;
    }

    public Username getUsername() {
        return this.username;
    }

    public Password getPassword() {
        return this.password;
    }

    public Scopes getScopes() {
        return scopes;
    }

}
