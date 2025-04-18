package dev.felix2000jp.springboottemplate.notes.domain.valueobjects;

import jakarta.persistence.Embeddable;
import org.springframework.util.Assert;

@Embeddable
public record Content(String value) {

    public Content {
        Assert.notNull(value, "Content must not be null!");
    }

}
