package ru.clevertec.port.output.port;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Порт-команда для создания новости.
 */
@Builder
public record NewsCreatePortCommand(
        String title,
        String text,
        LocalDateTime time,
        UUID authorId) {
}
