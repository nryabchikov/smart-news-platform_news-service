package ru.clevertec.port.input.news.command;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Команда для создания новости.
 */
@Builder
public record NewsCreateCommand(
        String title,
        String text,
        LocalDateTime time,
        UUID authorId) {
}
