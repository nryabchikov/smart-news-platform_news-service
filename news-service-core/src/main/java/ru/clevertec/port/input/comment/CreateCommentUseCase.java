package ru.clevertec.port.input.comment;

import ru.clevertec.port.input.comment.command.CommentCreateCommand;
import ru.clevertec.port.input.comment.command.CommentUseCaseResult;

import java.util.UUID;

/**
 * Интерфейс Service для создания комментария.
 */
public interface CreateCommentUseCase {

    /**
     * Создает новый комментарий.
     *
     * @param newsId               ID новости, к которой относится комментарий.
     * @param commentCreateCommand Команда создания комментария.
     * @return Результат выполнения use case.
     */
    CommentUseCaseResult createComment(UUID newsId, CommentCreateCommand commentCreateCommand);
}