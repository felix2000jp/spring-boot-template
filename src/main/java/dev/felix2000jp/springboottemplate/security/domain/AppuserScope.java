package dev.felix2000jp.springboottemplate.security.domain;

import dev.felix2000jp.springboottemplate.security.domain.valueobjects.Scope;
import dev.felix2000jp.springboottemplate.security.domain.valueobjects.Identity;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import org.jmolecules.ddd.types.Entity;

@jakarta.persistence.Table(name = "appuser_scope")
@jakarta.persistence.Entity
public class AppuserScope implements Entity<Appuser, Identity> {

    @EmbeddedId
    private Identity id;

    @Embedded
    private Scope scope;

    protected AppuserScope() {
    }

    public AppuserScope(Identity id, Scope scope) {
        this.id = id;
        this.scope = scope;
    }

    @Override
    public Identity getId() {
        return this.id;
    }

    public Scope getScope() {
        return this.scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

}