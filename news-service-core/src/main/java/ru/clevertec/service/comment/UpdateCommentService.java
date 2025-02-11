package ru.clevertec.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.clevertec.client.comment.CommentClient;
import ru.clevertec.mapper.DomainCommentMapper;
import ru.clevertec.port.input.comment.UpdateCommentUseCase;
import ru.clevertec.port.input.comment.command.CommentUpdateCommand;
import ru.clevertec.port.input.comment.command.CommentUseCaseResult;

import java.util.UUID;

/**
 * Сервис для обновления комментариев. Реализует {@link UpdateCommentUseCase}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateCommentService implements UpdateCommentUseCase {

    private final CommentClient commentClient;
    private final DomainCommentMapper commentMapper;

    @Override
    public CommentUseCaseResult updateComment(UUID newsId, CommentUpdateCommand commentUpdateCommand) {
        log.info("UpdateCommentService.updateComment: Updating comment. newsId: {}, command: {}", newsId,
                commentUpdateCommand);
        CommentUseCaseResult result = commentMapper.toCommentUseCaseResult(
                commentClient.updateComment(newsId, commentMapper.toCommentUpdateRequest(commentUpdateCommand))
        );
        log.info("UpdateCommentService.updateComment: Comment updated successfully. Result: {}", result);
        return result;
    }
}
