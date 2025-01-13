package ru.clevertec.service.comment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.client.comment.CommentClient;
import ru.clevertec.client.comment.dto.CommentResponse;
import ru.clevertec.client.comment.dto.CommentUpdateRequest;
import ru.clevertec.mapper.DomainCommentMapper;
import ru.clevertec.port.input.comment.command.CommentUpdateCommand;
import ru.clevertec.port.input.comment.command.CommentUseCaseResult;
import ru.clevertec.util.comment.TestData;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateCommentServiceTest {

    @Mock
    CommentClient commentClient;

    @Mock
    DomainCommentMapper commentMapper;

    @InjectMocks
    UpdateCommentService updateCommentService;

    @Test
    void shouldUpdateComment() {
        //given
        UUID newsId = UUID.randomUUID();
        CommentResponse commentResponse = TestData.generateCommentResponse();
        CommentUpdateCommand commentUpdateCommand = TestData.generateCommentUpdateCommand();
        CommentUseCaseResult commentUseCaseResult = TestData.generateCommentUseCaseResult();
        CommentUpdateRequest commentUpdateRequest = TestData.generateCommentUpdateRequest();

        when(commentMapper.toCommentUpdateRequest(commentUpdateCommand))
                .thenReturn(commentUpdateRequest);
        when(commentClient.updateComment(newsId, commentUpdateRequest))
                .thenReturn(commentResponse);
        when(commentMapper.toCommentUseCaseResult(commentResponse))
                .thenReturn(commentUseCaseResult);

        //when
        CommentUseCaseResult actualCommentUseCaseResult =
                updateCommentService.updateComment(newsId, commentUpdateCommand);

        //then
        assertEquals(commentUseCaseResult, actualCommentUseCaseResult);
        verify(commentMapper).toCommentUpdateRequest(any());
        verify(commentClient).updateComment(any(), any());
        verify(commentMapper).toCommentUseCaseResult(any());
    }
}