package ru.clevertec.port.input.news.command;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Результат выполнения service для новости.
 */
@Builder
public record NewsUseCaseResult(
        UUID id,
        String title,
        String text,
        LocalDateTime time,
        UUID authorId
) {
}
