package ru.clevertec.service.comment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.client.comment.CommentClient;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class DeleteCommentServiceTest {

    @Mock
    CommentClient commentClient;

    @InjectMocks
    DeleteCommentService deleteCommentService;

    @Test
    void shouldDeleteComment() {
        //given
        UUID newsId = UUID.randomUUID();
        UUID commentId = UUID.randomUUID();

        //when
        deleteCommentService.deleteComment(newsId, commentId);

        //then
        verify(commentClient).deleteCommentById(any(), any());
        verifyNoMoreInteractions(commentClient);
    }
}