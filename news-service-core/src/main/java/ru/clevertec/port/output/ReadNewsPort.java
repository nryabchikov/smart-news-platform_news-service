package ru.clevertec.port.output;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.port.output.port.NewsPortResult;

import java.util.UUID;

/**
 * Порт для чтения новостей.
 */
public interface ReadNewsPort {
    /**
     * Возвращает постраничный список всех новостей.
     *
     * @param pageable Параметры пагинации.
     * @return Список новостей.
     */
    Page<NewsPortResult> readAllNews(Pageable pageable);

    /**
     * Возвращает новость по её ID.
     *
     * @param newsId ID новости.
     * @return Новость.
     */
    NewsPortResult readNewsById(UUID newsId);
}
