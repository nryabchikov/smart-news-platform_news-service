package ru.clevertec.client.user.dto;

import lombok.Builder;

import java.util.UUID;

/**
 * DTO для запроса на создание или обновление пользователя.
 */
@Builder
public record UserRequest(
        UUID keycloakUserId,
        String username
) {
}



