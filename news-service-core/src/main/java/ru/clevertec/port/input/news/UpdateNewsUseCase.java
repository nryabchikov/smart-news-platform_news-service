package ru.clevertec.port.input.news;

import ru.clevertec.port.input.news.command.NewsUpdateCommand;
import ru.clevertec.port.input.news.command.NewsUseCaseResult;

/**
 * Интерфейс Service для обновления новости.
 */
public interface UpdateNewsUseCase {
    /**
     * Обновляет новость.
     *
     * @param newsUpdateCommand Команда обновления новости.
     * @return Результат обновления новости.
     */
    NewsUseCaseResult updateNews(NewsUpdateCommand newsUpdateCommand);
}
