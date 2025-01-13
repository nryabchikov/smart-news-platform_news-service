package ru.clevertec.service.news;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.cache.Cache;
import ru.clevertec.client.user.UserClient;
import ru.clevertec.domain.News;
import ru.clevertec.mapper.DomainNewsMapper;
import ru.clevertec.port.input.news.CreateNewsUseCase;
import ru.clevertec.port.input.news.command.NewsCreateCommand;
import ru.clevertec.port.input.news.command.NewsUseCaseResult;
import ru.clevertec.port.output.WriteNewsPort;

import java.util.UUID;

/**
 * Сервис для создания новостей. Реализует {@link CreateNewsUseCase}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreateNewsService implements CreateNewsUseCase {

    private final Cache<UUID, News> cache;
    private final WriteNewsPort newsPort;
    private final DomainNewsMapper newsMapper;
    private final UserClient userClient;

    @Override
    @Transactional
    public NewsUseCaseResult createNews(NewsCreateCommand newsCreateCommand, UUID keycloakUserId) {
        log.info("CreateNewsService.createNews: Creating news. command: {}, keycloakUserId: {}",
                newsCreateCommand, keycloakUserId);
        UUID authorId = userClient.readUserByKeycloakId(keycloakUserId).id();
        News news = newsMapper.toNews(newsCreateCommand);
        news.setId(UUID.randomUUID());
        news.setAuthorId(authorId);
        cache.put(news.getId(), news);
        NewsUseCaseResult result =
                newsMapper.toNewsUseCaseResult(newsPort.createNews(newsMapper.toNewsCreatePortCommand(news)));
        log.info("CreateNewsService.createNews: News created successfully. Result: {}", result);
        return result;
    }
}
