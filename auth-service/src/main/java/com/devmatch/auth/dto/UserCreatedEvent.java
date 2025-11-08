package com.devmatch.auth.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UserCreatedEvent(
    UUID eventId,
    Instant occurredAt,
    Long userId,
    String email,
    String firstName,
    String lastName,
    Set<String> roles
) {}
