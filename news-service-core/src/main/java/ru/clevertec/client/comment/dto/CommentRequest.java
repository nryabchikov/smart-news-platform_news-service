package ru.clevertec.client.comment.dto;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * DTO для запроса на создание комментария.
 */
@Builder
public record CommentRequest(
        String text,
        LocalDateTime time
) {
}