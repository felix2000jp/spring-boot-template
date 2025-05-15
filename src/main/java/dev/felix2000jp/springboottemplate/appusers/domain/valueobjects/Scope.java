package dev.felix2000jp.springboottemplate.appusers.domain.valueobjects;

import dev.felix2000jp.springboottemplate.system.security.SecurityScope;
import org.jmolecules.ddd.types.ValueObject;
import org.springframework.util.Assert;

public record Scope(SecurityScope value) implements ValueObject {

    public Scope {
        Assert.notNull(value, "Scope cannot be null");
    }

}
