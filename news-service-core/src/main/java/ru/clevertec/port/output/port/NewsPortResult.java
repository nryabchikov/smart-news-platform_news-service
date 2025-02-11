package ru.clevertec.port.output.port;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Результат получения новости.
 */
@Builder
public record NewsPortResult(
        UUID id,
        String title,
        String text,
        LocalDateTime time,
        UUID authorId) {
}
