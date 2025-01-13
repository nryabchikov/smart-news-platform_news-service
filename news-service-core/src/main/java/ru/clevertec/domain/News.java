package ru.clevertec.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Доменная модель для новости.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class News {
    /**
     * ID новости.
     */
    UUID id;

    /**
     * Заголовок новости.
     */
    String title;

    /**
     * Текст новости.
     */
    String text;

    /**
     * Дата и время публикации новости.
     */
    LocalDateTime time;

    /**
     * ID автора новости.
     */
    UUID authorId;
}
