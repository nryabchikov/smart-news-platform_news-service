package ru.clevertec.port.input.comment;

import ru.clevertec.port.input.comment.command.CommentUpdateCommand;
import ru.clevertec.port.input.comment.command.CommentUseCaseResult;

import java.util.UUID;

/**
 * Интерфейс Service для обновления комментария.
 */
public interface UpdateCommentUseCase {

    /**
     * Обновляет комментарий.
     *
     * @param newsId               ID новости, к которой относится комментарий.
     * @param commentUpdateCommand Команда обновления комментария.
     * @return Результат выполнения use case.
     */
    CommentUseCaseResult updateComment(UUID newsId, CommentUpdateCommand commentUpdateCommand);
}
