package dev.felix2000jp.springboottemplate.appusers.domain.valueobjects;

import org.jmolecules.ddd.types.Identifier;
import org.springframework.util.Assert;

import java.util.UUID;

public record AppuserId(UUID value) implements Identifier {

    public AppuserId {
        Assert.notNull(value, "AppuserId cannot be null");
    }

}

