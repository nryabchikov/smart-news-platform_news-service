package ru.clevertec.service.news;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.cache.Cache;
import ru.clevertec.client.comment.CommentClient;
import ru.clevertec.domain.News;
import ru.clevertec.port.input.news.DeleteNewsUseCase;
import ru.clevertec.port.output.WriteNewsPort;

import java.util.UUID;

/**
 * Сервис для удаления новостей. Реализует {@link DeleteNewsUseCase}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteNewsService implements DeleteNewsUseCase {

    private final Cache<UUID, News> cache;
    private final CommentClient commentClient;
    private final WriteNewsPort newsPort;

    @Override
    @Transactional
    public void deleteNews(UUID newsId) {
        log.info("DeleteNewsService.deleteNews: Deleting news. newsId: {}", newsId);
        newsPort.deleteNewsById(newsId);
        cache.delete(newsId);
        commentClient.deleteAllComments(newsId);
        log.info("DeleteNewsService.deleteNews: News deleted successfully.");
    }
}
