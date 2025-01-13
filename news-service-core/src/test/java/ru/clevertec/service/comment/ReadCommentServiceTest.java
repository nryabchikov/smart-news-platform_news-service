package ru.clevertec.service.comment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.client.comment.CommentClient;
import ru.clevertec.client.comment.dto.CommentResponse;
import ru.clevertec.mapper.DomainCommentMapper;
import ru.clevertec.port.input.comment.command.CommentUseCaseResult;
import ru.clevertec.util.comment.TestData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReadCommentServiceTest {

    @Mock
    CommentClient commentClient;

    @Mock
    DomainCommentMapper commentMapper;

    @InjectMocks
    ReadCommentService readCommentService;

    @Test
    void shouldReadAllCommentsByNewsId() {
        // given
        int pageNumber = 0;
        int pageSize = 3;
        UUID newsId = UUID.randomUUID();

        List<CommentResponse> commentResponses = TestData.generateListOfCommentResponses();
        List<CommentUseCaseResult> useCaseResults = new ArrayList<>();

        when(commentClient.readAllComments(newsId, pageNumber, pageSize))
                .thenReturn(commentResponses);

        for (CommentResponse response : commentResponses) {
            CommentUseCaseResult mappedResult = TestData.generateCommentUseCaseResult();
            when(commentMapper.toCommentUseCaseResult(response))
                    .thenReturn(mappedResult);
            useCaseResults.add(mappedResult);
        }

        // when
        List<CommentUseCaseResult> actualCommentUseCaseResults =
                readCommentService.readAllCommentsByNewsId(newsId, pageNumber, pageSize);

        // then
        assertEquals(useCaseResults, actualCommentUseCaseResults);
        verify(commentClient).readAllComments(newsId, pageNumber, pageSize);
        verify(commentMapper, times(commentResponses.size())).toCommentUseCaseResult(any());
    }


    @Test
    void shouldReadCommentByIdAndNewsId() {
        //given
        UUID commentId = UUID.randomUUID();
        UUID newsId = UUID.randomUUID();
        CommentResponse commentResponse = TestData.generateCommentResponse();
        CommentUseCaseResult commentUseCaseResult = TestData.generateCommentUseCaseResult();


        when(commentClient.readCommentById(newsId, commentId))
               .thenReturn(commentResponse);
        when(commentMapper.toCommentUseCaseResult(commentResponse))
               .thenReturn(commentUseCaseResult);

        //when
        CommentUseCaseResult actualCommentUseCaseResult =
                readCommentService.readCommentByIdAndNewsId(commentId, newsId);

        //then
        assertEquals(commentUseCaseResult, actualCommentUseCaseResult);
        verify(commentClient).readCommentById(any(), any());
        verify(commentMapper).toCommentUseCaseResult(any());
    }
}