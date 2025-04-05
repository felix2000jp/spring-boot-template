package dev.felix2000jp.springboottemplate.appusers.domain.valueobjects;

import jakarta.persistence.Embeddable;
import org.jmolecules.ddd.types.Identifier;
import org.springframework.util.Assert;

import java.util.UUID;

@Embeddable
public record Identity(UUID id) implements Identifier {

    public Identity {
        Assert.notNull(id, "Identity value cannot be null or blank.");
    }

}
