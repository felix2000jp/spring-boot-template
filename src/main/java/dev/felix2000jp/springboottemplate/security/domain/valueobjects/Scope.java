package dev.felix2000jp.springboottemplate.security.domain.valueobjects;

import dev.felix2000jp.springboottemplate.security.SecurityScope;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.jmolecules.ddd.types.ValueObject;
import org.springframework.util.Assert;

@Embeddable
public record Scope(@Column(name = "scope") SecurityScope value) implements ValueObject {

    public Scope {
        Assert.notNull(value, "Scope cannot be null");
    }

}
