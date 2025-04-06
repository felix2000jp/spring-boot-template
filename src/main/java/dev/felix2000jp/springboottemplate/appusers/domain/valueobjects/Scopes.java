package dev.felix2000jp.springboottemplate.appusers.domain.valueobjects;

import dev.felix2000jp.springboottemplate.shared.security.SecurityScope;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.jmolecules.ddd.types.ValueObject;
import org.springframework.util.Assert;

import java.util.List;

@Embeddable
public record Scopes(@Column(name = "scopes") List<SecurityScope> value) implements ValueObject {

    public Scopes {
        Assert.notNull(value, "Scopes cannot be null");
        Assert.notEmpty(value, "Scopes cannot be empty");
    }

}
