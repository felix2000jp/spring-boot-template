package dev.felix2000jp.springboottemplate.appusers.domain.events;

import java.util.UUID;

public record AppuserCreatedEvent(UUID appuserId) {
}
