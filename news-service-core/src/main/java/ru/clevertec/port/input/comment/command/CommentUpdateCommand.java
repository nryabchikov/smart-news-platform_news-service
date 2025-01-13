package ru.clevertec.port.input.comment.command;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Команда для обновления комментария.
 */
@Builder
public record CommentUpdateCommand(
        UUID id,
        String text,
        LocalDateTime time,
        UUID newsId,
        UUID authorId
) {}
