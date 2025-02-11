package ru.clevertec.client.user.dto;

import java.util.UUID;

/**
 * DTO для представления информации о пользователе.
 */
public record UserResponse(
        UUID id,
        UUID keycloakUserId,
        String username
) {}


