package ru.clevertec.service.news;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.cache.Cache;
import ru.clevertec.domain.News;
import ru.clevertec.mapper.DomainNewsMapper;
import ru.clevertec.port.input.news.UpdateNewsUseCase;
import ru.clevertec.port.input.news.command.NewsUpdateCommand;
import ru.clevertec.port.input.news.command.NewsUseCaseResult;
import ru.clevertec.port.output.WriteNewsPort;

import java.util.UUID;

/**
 * Сервис для обновления новостей. Реализует {@link UpdateNewsUseCase}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateNewsService implements UpdateNewsUseCase {

    private final Cache<UUID, News> cache;
    private final WriteNewsPort newsPort;
    private final DomainNewsMapper newsMapper;

    @Override
    @Transactional
    public NewsUseCaseResult updateNews(NewsUpdateCommand newsUpdateCommand) {
        log.info("UpdateNewsService.updateNews: Updating news. command: {}", newsUpdateCommand);
        News news = newsMapper.toNews(newsUpdateCommand);
        cache.put(news.getId(), news);
        NewsUseCaseResult result = newsMapper.toNewsUseCaseResult(
                newsPort.updateNews(newsMapper.toNewsUpdatePortCommand(news))
        );
        log.info("UpdateNewsService.updateNews: News updated successfully. Result: {}", result);
        return result;
    }
}
