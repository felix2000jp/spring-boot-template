package dev.felix2000jp.springboottemplate.security.domain;

import dev.felix2000jp.springboottemplate.security.domain.valueobjects.Password;
import dev.felix2000jp.springboottemplate.security.domain.valueobjects.Identity;
import dev.felix2000jp.springboottemplate.security.domain.valueobjects.Username;
import jakarta.persistence.*;
import org.jmolecules.ddd.types.AggregateRoot;

import java.util.HashSet;
import java.util.Set;

@jakarta.persistence.Table(name = "appuser")
@jakarta.persistence.Entity
public class Appuser implements AggregateRoot<Appuser, Identity> {

    @EmbeddedId
    private Identity id;

    @Embedded
    private Username username;

    @Embedded
    private Password password;

    @JoinColumn(name = "appuser_id")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AppuserScope> scopes;

    protected Appuser() {
    }

    public Appuser(Identity id, Username username, Password password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.scopes = new HashSet<>();
    }

    @Override
    public Identity getId() {
        return this.id;
    }

    public Username getUsername() {
        return this.username;
    }

    public void setUsername(Username username) {
        this.username = username;
    }

    public Password getPassword() {
        return this.password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public Set<AppuserScope> getScopes() {
        return scopes;
    }

}
