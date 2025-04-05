package dev.felix2000jp.springboottemplate.security.domain.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.jmolecules.ddd.types.ValueObject;
import org.springframework.util.Assert;

@Embeddable
public record Username(@Column(name = "username") String value) implements ValueObject {

    public Username {
        Assert.notNull(value, "Username cannot be null");
        Assert.isTrue(value.length() >= 3, "Username must be at least 3 characters");
        Assert.isTrue(value.length() <= 255, "Username must be at max 255 characters");
    }

}
