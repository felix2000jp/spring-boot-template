package dev.felix2000jp.springboottemplate.appusers.domain.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.jmolecules.ddd.types.Identifier;
import org.springframework.util.Assert;

import java.util.UUID;

@Embeddable
public record AppuserScopeId(@Column(name = "id") UUID value) implements Identifier {

    public AppuserScopeId {
        Assert.notNull(value, "AppuserScopeId value cannot be null or blank.");
    }

}
