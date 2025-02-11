package ru.clevertec.port.input.news;

import org.springframework.data.domain.Page;
import ru.clevertec.port.input.news.command.NewsUseCaseResult;

import java.util.UUID;

/**
 * Интерфейс Service для чтения новостей.
 */
public interface ReadNewsUseCase {
    /**
     * Возвращает постраничный список всех новостей.
     *
     * @param pageNumber Номер страницы.
     * @param pageSize   Размер страницы.
     * @return Список новостей.
     */
    Page<NewsUseCaseResult> readAllNews(int pageNumber, int pageSize);

    /**
     * Возвращает новость по её ID.
     *
     * @param newsId ID новости.
     * @return Новость.
     */
    NewsUseCaseResult readNewsById(UUID newsId);
}
