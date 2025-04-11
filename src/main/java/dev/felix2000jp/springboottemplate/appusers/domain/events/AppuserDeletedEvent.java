package dev.felix2000jp.springboottemplate.appusers.domain.events;

import org.jmolecules.event.types.DomainEvent;

import java.util.UUID;

public record AppuserDeletedEvent(UUID appuserId) implements DomainEvent {
}
