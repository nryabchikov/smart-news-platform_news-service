package ru.clevertec.port.input.comment;

import ru.clevertec.port.input.comment.command.CommentUseCaseResult;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс Service для чтения комментариев.
 */
public interface ReadCommentUseCase {

    /**
     * Возвращает список всех комментариев к указанной новости, используя пагинацию.
     *
     * @param newsId     ID новости.
     * @param pageNumber Номер страницы.
     * @param pageSize   Размер страницы.
     * @return Список комментариев {@link CommentUseCaseResult}.
     */
    List<CommentUseCaseResult> readAllCommentsByNewsId(UUID newsId, int pageNumber, int pageSize);

    /**
     * Возвращает комментарий по его ID и по ID новости.
     *
     * @param commentId ID комментария.
     * @param newsId    ID новости.
     * @return Комментарий {@link CommentUseCaseResult}.
     */
    CommentUseCaseResult readCommentByIdAndNewsId(UUID commentId, UUID newsId);
}
