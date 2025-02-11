package ru.clevertec.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.clevertec.client.comment.CommentClient;
import ru.clevertec.port.input.comment.DeleteCommentUseCase;

import java.util.UUID;

/**
 * Сервис для удаления комментариев. Реализует {@link DeleteCommentUseCase}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteCommentService implements DeleteCommentUseCase {

    private final CommentClient commentClient;

    @Override
    public void deleteComment(UUID newsId, UUID commentId) {
        log.info("DeleteCommentService.deleteComment: Deleting comment. newsId: {}, commentId: {}", newsId, commentId);
        commentClient.deleteCommentById(newsId, commentId);
        log.info("DeleteCommentService.deleteComment: Comment deleted successfully.");
    }
}
