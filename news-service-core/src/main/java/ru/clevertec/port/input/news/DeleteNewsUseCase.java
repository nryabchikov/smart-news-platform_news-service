package ru.clevertec.port.input.news;

import java.util.UUID;

/**
 * Интерфейс Service для удаления новости.
 */
public interface DeleteNewsUseCase {
    /**
     * Удаляет новость по её ID.
     *
     * @param newsId ID удаляемой новости.
     */
    void deleteNews(UUID newsId);
}
