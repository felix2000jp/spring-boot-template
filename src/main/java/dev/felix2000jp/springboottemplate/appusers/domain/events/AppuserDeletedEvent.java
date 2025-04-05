package dev.felix2000jp.springboottemplate.appusers.domain.events;

import java.util.UUID;

public record AppuserDeletedEvent(UUID appuserId) {
}
