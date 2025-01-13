package ru.clevertec.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.clevertec.client.comment.CommentClient;
import ru.clevertec.mapper.DomainCommentMapper;
import ru.clevertec.port.input.comment.ReadCommentUseCase;
import ru.clevertec.port.input.comment.command.CommentUseCaseResult;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для чтения комментариев. Реализует {@link ReadCommentUseCase}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReadCommentService implements ReadCommentUseCase {

    private final CommentClient commentClient;
    private final DomainCommentMapper commentMapper;

    @Override
    public List<CommentUseCaseResult> readAllCommentsByNewsId(UUID newsId, int pageNumber, int pageSize) {
        log.info("ReadCommentService.readAllCommentsByNewsId: Reading comments. newsId: {}, pageNumber: {}, pageSize: {}",
                newsId, pageNumber, pageSize);
        List<CommentUseCaseResult> results = commentClient.readAllComments(newsId, pageNumber, pageSize).stream()
                .map(commentMapper::toCommentUseCaseResult)
                .toList();
        log.info("ReadCommentService.readAllCommentsByNewsId: Comments read successfully. Count: {}", results.size());
        return results;
    }

    @Override
    public CommentUseCaseResult readCommentByIdAndNewsId(UUID commentId, UUID newsId) {
        log.info("ReadCommentService.readCommentByIdAndNewsId: Reading comment. commentId: {}, newsId: {}", commentId,
                newsId);
        CommentUseCaseResult result = commentMapper.toCommentUseCaseResult(commentClient.readCommentById(newsId,
                commentId));
        log.info("ReadCommentService.readCommentByIdAndNewsId: Comment read successfully. Result: {}", result);
        return result;
    }
}
