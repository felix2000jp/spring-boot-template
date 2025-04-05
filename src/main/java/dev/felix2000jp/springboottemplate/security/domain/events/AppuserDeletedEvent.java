package dev.felix2000jp.springboottemplate.security.domain.events;

import java.util.UUID;

public record AppuserDeletedEvent(UUID appuserId) {
}
