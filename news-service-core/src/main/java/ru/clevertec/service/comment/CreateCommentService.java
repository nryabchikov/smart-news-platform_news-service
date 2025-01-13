package ru.clevertec.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.clevertec.client.comment.CommentClient;
import ru.clevertec.mapper.DomainCommentMapper;
import ru.clevertec.port.input.comment.CreateCommentUseCase;
import ru.clevertec.port.input.comment.command.CommentCreateCommand;
import ru.clevertec.port.input.comment.command.CommentUseCaseResult;

import java.util.UUID;

/**
 * Сервис для создания комментариев. Реализует {@link CreateCommentUseCase}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreateCommentService implements CreateCommentUseCase {

    private final CommentClient commentClient;
    private final DomainCommentMapper commentMapper;


    @Override
    public CommentUseCaseResult createComment(UUID newsId, CommentCreateCommand commentCreateCommand) {
        log.info("CreateCommentService.createComment: Creating comment for newsId: {}, command: {}", newsId,
                commentCreateCommand);
        CommentUseCaseResult result = commentMapper.toCommentUseCaseResult(
                commentClient.createComment(newsId, commentMapper.toCommentRequest(commentCreateCommand))
        );
        log.info("CreateCommentService.createComment: Comment created successfully. Result: {}", result);
        return result;
    }
}
