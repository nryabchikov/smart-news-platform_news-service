package ru.clevertec.service.comment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.client.comment.CommentClient;
import ru.clevertec.client.comment.dto.CommentRequest;
import ru.clevertec.client.comment.dto.CommentResponse;
import ru.clevertec.mapper.DomainCommentMapper;
import ru.clevertec.port.input.comment.command.CommentCreateCommand;
import ru.clevertec.port.input.comment.command.CommentUseCaseResult;
import ru.clevertec.util.comment.TestData;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCommentServiceTest {

    @Mock
    CommentClient commentClient;

    @Mock
    DomainCommentMapper commentMapper;

    @InjectMocks
    CreateCommentService createCommentService;

    @Test
    void shouldCreateComment() {
        //given
        UUID newsId = UUID.randomUUID();
        CommentCreateCommand commentCreateCommand = TestData.generateCommentCreateCommand();
        CommentUseCaseResult commentUseCaseResult = TestData.generateCommentUseCaseResult();
        CommentRequest commentRequest = TestData.generateCommentRequest();
        CommentResponse commentResponse = TestData.generateCommentResponse();

        when(commentMapper.toCommentRequest(commentCreateCommand))
                .thenReturn(commentRequest);
        when(commentClient.createComment(newsId, commentRequest))
                .thenReturn(commentResponse);
        when(commentMapper.toCommentUseCaseResult(commentResponse))
                .thenReturn(commentUseCaseResult);

        //when
        CommentUseCaseResult actualCommentUseCaseResult =
                createCommentService.createComment(newsId, commentCreateCommand);

        //then
        assertEquals(commentUseCaseResult, actualCommentUseCaseResult);
        verify(commentMapper).toCommentRequest(any());
        verify(commentClient).createComment(any(), any());
        verify(commentMapper).toCommentUseCaseResult(any());
    }
}