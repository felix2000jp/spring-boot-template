package dev.felix2000jp.springboottemplate.notes.domain.valueobjects;

import jakarta.persistence.Embeddable;
import org.springframework.util.Assert;

@Embeddable
public record Title(String value) {

    public Title {
        Assert.notNull(value, "Title must not be null");
    }

}
