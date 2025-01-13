package ru.clevertec.port.input.news;

import ru.clevertec.port.input.news.command.NewsCreateCommand;
import ru.clevertec.port.input.news.command.NewsUseCaseResult;

import java.util.UUID;

/**
 * Интерфейс Service для создания новости.
 */
public interface CreateNewsUseCase {
    /**
     * Создает новую новость.
     *
     * @param newsCreateCommand Команда создания новости.
     * @param keycloakUserId    ID пользователя, создающего новость.
     * @return Результат создания новости.
     */
    NewsUseCaseResult createNews(NewsCreateCommand newsCreateCommand, UUID keycloakUserId);
}
