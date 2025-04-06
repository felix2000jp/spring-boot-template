package dev.felix2000jp.springboottemplate.appusers.domain.valueobjects;

import dev.felix2000jp.springboottemplate.shared.security.SecurityScope;
import jakarta.persistence.Embeddable;
import org.jmolecules.ddd.types.ValueObject;
import org.springframework.util.Assert;

@Embeddable
public record Scope(SecurityScope value) implements ValueObject {

    public Scope {
        Assert.notNull(value, "Scope cannot be null");
    }

}
