package ru.clevertec.adapter.input.web.news.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO класс для представления новости в ответе.
 */
@Builder
public record NewsResponse(
        UUID id,
        String title,
        String text,
        LocalDateTime time,
        UUID authorId
) {}
