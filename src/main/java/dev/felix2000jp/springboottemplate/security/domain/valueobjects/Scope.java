package dev.felix2000jp.springboottemplate.security.domain.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.jmolecules.ddd.types.ValueObject;
import org.springframework.util.Assert;

@Embeddable
public record Scope(@Column(name = "scope") String value) implements ValueObject {

    public Scope {
        Assert.notNull(value, "Scope cannot be null");
    }

}
