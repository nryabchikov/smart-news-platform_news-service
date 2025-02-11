package ru.clevertec.port.output.port;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Команда для обновления новости.
 */
@Builder
public record NewsUpdatePortCommand(
        UUID id,
        String title,
        String text,
        LocalDateTime time,
        UUID authorId) {
}
