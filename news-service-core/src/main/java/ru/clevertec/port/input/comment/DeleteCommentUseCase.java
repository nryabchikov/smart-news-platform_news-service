package ru.clevertec.port.input.comment;

import java.util.UUID;

/**
 * Интерфейс Service для удаления комментария.
 */
public interface DeleteCommentUseCase {

    /**
     * Удаляет комментарий.
     *
     * @param newsId    ID новости, к которой относится комментарий.
     * @param commentId ID комментария.
     */
    void deleteComment(UUID newsId, UUID commentId);
}
