package dev.felix2000jp.springboottemplate.appusers.domain.valueobjects;

import dev.felix2000jp.springboottemplate.system.security.SecurityScope;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.jmolecules.ddd.types.ValueObject;
import org.springframework.util.Assert;

@Embeddable
public record Scope(
        @Enumerated(EnumType.STRING)
        SecurityScope scope
) implements ValueObject {

    public Scope {
        Assert.notNull(scope, "Scope cannot be null");
    }

}
