package ru.clevertec.service.news;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.clevertec.cache.Cache;
import ru.clevertec.domain.News;
import ru.clevertec.mapper.DomainNewsMapper;
import ru.clevertec.port.input.news.ReadNewsUseCase;
import ru.clevertec.port.input.news.command.NewsUseCaseResult;
import ru.clevertec.port.output.ReadNewsPort;
import ru.clevertec.port.output.port.NewsPortResult;

import java.util.UUID;

/**
 * Сервис для чтения новостей. Реализует {@link ReadNewsUseCase}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReadNewsService implements ReadNewsUseCase {

    private final ReadNewsPort newsPort;
    private final DomainNewsMapper newsMapper;
    private final Cache<UUID, News> cache;

    @Override
    public Page<NewsUseCaseResult> readAllNews(int pageNumber, int pageSize) {
        log.info("ReadNewsService.readAllNews: Reading all news. pageNumber: {}, pageSize: {}", pageNumber, pageSize);
        Page<NewsUseCaseResult> result = newsPort.readAllNews(PageRequest.of(pageNumber, pageSize))
                .map(newsMapper::toNewsUseCaseResult);
        log.info("ReadNewsService.readAllNews: News read successfully. Count: {}", result.getTotalElements());
        return result;
    }

    @Override
    public NewsUseCaseResult readNewsById(UUID newsId) {
        log.info("ReadNewsService.readNewsById: Reading news by id. newsId: {}", newsId);
        NewsUseCaseResult result = cache.get(newsId)
                .map(newsMapper::toNewsUseCaseResult)
                .orElseGet(() -> {
                    NewsPortResult newsPortResult = newsPort.readNewsById(newsId);
                    News news = newsMapper.toNews(newsPortResult);
                    cache.put(newsId, news);
                    return newsMapper.toNewsUseCaseResult(news);
                });
        log.info("ReadNewsService.readNewsById: News read successfully. Result: {}", result);
        return result;
    }
}
