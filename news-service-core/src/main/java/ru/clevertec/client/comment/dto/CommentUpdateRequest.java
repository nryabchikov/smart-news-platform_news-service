package ru.clevertec.client.comment.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO для запроса на обновление комментария.
 */
@Builder
public record CommentUpdateRequest(
        UUID id,
        String text,
        LocalDateTime time
) {
}
