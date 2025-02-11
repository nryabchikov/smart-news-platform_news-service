package ru.clevertec.exception;

import java.util.UUID;

/**
 * Кастомное исключение, выбрасываемое, когда новость не найдена.
 */
public class NewsNotFoundException extends RuntimeException {
    private NewsNotFoundException(String message) {
        super(message);
    }

    /**
     * Создает новое исключение NewsNotFoundException с сообщением об ошибке.
     *
     * @param id ID новости, которая не найдена.
     * @return Экземпляр NewsNotFoundException.
     */
    public static NewsNotFoundException byId(UUID id) {
        return new NewsNotFoundException(String.format("News with id %s not found", id));
    }
}
