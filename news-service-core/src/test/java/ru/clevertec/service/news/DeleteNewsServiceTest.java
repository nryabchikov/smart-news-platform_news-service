package ru.clevertec.service.news;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.cache.Cache;
import ru.clevertec.client.comment.CommentClient;
import ru.clevertec.domain.News;
import ru.clevertec.port.output.WriteNewsPort;
import ru.clevertec.service.news.DeleteNewsService;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class DeleteNewsServiceTest {

    @Mock
    Cache<UUID, News> cache;

    @Mock
    WriteNewsPort newsPort;

    @Mock
    CommentClient commentClient;

    @InjectMocks
    DeleteNewsService deleteNewsService;

    @Test
    void shouldDeleteNewsById() {
        //given
        UUID newsId = UUID.randomUUID();

        //when
        deleteNewsService.deleteNews(newsId);

        //then
        verify(newsPort).deleteNewsById(newsId);
        verify(cache).delete(newsId);
        verify(commentClient).deleteAllComments(newsId);
        verifyNoMoreInteractions(newsPort);
    }
}
