package ru.clevertec.adapter.output.persistence.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.clevertec.adapter.output.persistence.PersistenceNewsMapper;
import ru.clevertec.adapter.output.persistence.jpa.repository.NewsRepository;
import ru.clevertec.exception.NewsNotFoundException;
import ru.clevertec.port.output.ReadNewsPort;
import ru.clevertec.port.output.port.NewsPortResult;

import java.util.UUID;

/**
 * Адаптер для чтения новостей из базы данных.  Реализует интерфейс {@link ReadNewsPort}.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReadNewsAdapter implements ReadNewsPort {

    /**
     * Репозиторий для доступа к базе данных с новостями.
     */
    private final NewsRepository newsRepository;

    /**
     * Маппер для преобразования сущностей Entity в объекты Port.
     */
    private final PersistenceNewsMapper newsMapper;

    /**
     * Возвращает список всех новостей, используя пагинацию.
     *
     * @param pageable Объект Pageable, определяющий параметры пагинации.
     * @return Страница результатов {@link NewsPortResult}.
     */
    @Override
    public Page<NewsPortResult> readAllNews(Pageable pageable) {
        log.info("ReadNewsAdapter.readAllNews: Retrieving all news with pagination: {}", pageable);
        Page<NewsPortResult> result = newsRepository.findAll(pageable)
                .map(newsMapper::toNewsPortResult);
        log.debug("ReadNewsAdapter.readAllNews: Retrieved {} news items.", result.getTotalElements());
        return result;
    }

    /**
     * Возвращает новость по её ID.
     *
     * @param newsId ID новости.
     * @return Новость {@link NewsPortResult}.
     * @throws NewsNotFoundException Если новость не найдена.
     */
    @Override
    public NewsPortResult readNewsById(UUID newsId) {
        log.info("ReadNewsAdapter.readNewsById: Retrieving news by ID: {}", newsId);
        NewsPortResult result = newsRepository.findById(newsId)
                .map(newsMapper::toNewsPortResult)
                .orElseThrow(() -> {
                    log.error("ReadNewsAdapter.readNewsById: News not found: {}", newsId);
                    return NewsNotFoundException.byId(newsId);
                });
        log.debug("ReadNewsAdapter.readNewsById: News item retrieved successfully: {}", result);
        return result;
    }
}
