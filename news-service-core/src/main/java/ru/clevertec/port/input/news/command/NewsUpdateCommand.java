package ru.clevertec.port.input.news.command;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Команда для обновления новости.
 */
@Builder
public record NewsUpdateCommand(
        UUID id,
        String title,
        String text,
        LocalDateTime time,
        UUID authorId) {
}
