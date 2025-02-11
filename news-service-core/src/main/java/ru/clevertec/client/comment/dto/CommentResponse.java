package ru.clevertec.client.comment.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO для представления комментария.
 */
@Builder
public record CommentResponse(
        UUID id,
        String text,
        LocalDateTime time,
        UUID newsId,
        UUID authorId
) {
}
