package dev.felix2000jp.springboottemplate.appusers.domain.valueobjects;

import jakarta.persistence.Embeddable;
import org.jmolecules.ddd.types.ValueObject;
import org.springframework.util.Assert;

@Embeddable
public record Username(String username) implements ValueObject {

    public Username {
        Assert.notNull(username, "Username cannot be null");
        Assert.isTrue(username.length() >= 3, "Username must be at least 3 characters");
        Assert.isTrue(username.length() <= 255, "Username must be at max 255 characters");
    }

}
