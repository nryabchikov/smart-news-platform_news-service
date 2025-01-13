package ru.clevertec.port.output;

import ru.clevertec.port.output.port.NewsCreatePortCommand;
import ru.clevertec.port.output.port.NewsPortResult;
import ru.clevertec.port.output.port.NewsUpdatePortCommand;

import java.util.UUID;

/**
 * Порт для записи новостей.
 */
public interface WriteNewsPort {
    /**
     * Создает новую новость.
     *
     * @param newsCreatePortCommand Команда создания новости.
     * @return Созданная новость.
     */
    NewsPortResult createNews(NewsCreatePortCommand newsCreatePortCommand);

    /**
     * Обновляет новость.
     *
     * @param newsUpdatePortCommand Команда обновления новости.
     * @return Обновленная новость.
     */
    NewsPortResult updateNews(NewsUpdatePortCommand newsUpdatePortCommand);

    /**
     * Удаляет новость по её ID.
     *
     * @param newsId ID новости.
     */
    void deleteNewsById(UUID newsId);
}