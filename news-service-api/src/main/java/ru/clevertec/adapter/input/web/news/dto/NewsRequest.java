package ru.clevertec.adapter.input.web.news.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * DTO класс для запроса на создание новости.
 */
@Builder
public record NewsRequest (
    @NotBlank(message = "Title should not be blank.") String title,
    @NotBlank(message = "Text should not be blank.") String text,

    @NotNull(message = "News date and time should be provided.")
    @PastOrPresent(message = "News date and time should be in the past or today.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime time
) {}
