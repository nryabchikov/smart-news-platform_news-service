package ru.clevertec.adapter.output.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.adapter.output.persistence.PersistenceNewsMapper;
import ru.clevertec.adapter.output.persistence.jpa.entity.NewsEntity;
import ru.clevertec.adapter.output.persistence.jpa.repository.NewsRepository;
import ru.clevertec.exception.NewsNotFoundException;
import ru.clevertec.port.output.WriteNewsPort;
import ru.clevertec.port.output.port.NewsCreatePortCommand;
import ru.clevertec.port.output.port.NewsPortResult;
import ru.clevertec.port.output.port.NewsUpdatePortCommand;

import java.util.UUID;

/**
 * Адаптер для записи новостей в базу данных. Реализует интерфейс {@link WriteNewsPort}.
 */
@Component
@RequiredArgsConstructor
public class WriteNewsAdapter implements WriteNewsPort {

    /**
     * Репозиторий для доступа к базе данных с новостями.
     */
    private final NewsRepository newsRepository;

    /**
     * Маппер для преобразования сущностей Entity в объекты Port.
     */
    private final PersistenceNewsMapper newsMapper;

    /**
     * Создает новую новость в базе данных.
     *
     * @param newsCreatePortCommand Порт-команда для создания новости.
     * @return Созданная новость {@link NewsPortResult}.
     */
    @Override
    public NewsPortResult createNews(NewsCreatePortCommand newsCreatePortCommand) {
        NewsEntity newsEntity = newsMapper.toNewsEntity(newsCreatePortCommand);
        return newsMapper.toNewsPortResult(newsRepository.save(newsEntity));
    }

    /**
     * Обновляет существующую новость в базе данных.
     *
     * @param newsUpdatePortCommand Порт-команда для обновления новости.
     * @return Обновленная новость {@link NewsPortResult}.
     * @throws NewsNotFoundException Если новость не найдена.
     */
    @Override
    public NewsPortResult updateNews(NewsUpdatePortCommand newsUpdatePortCommand) {
        NewsEntity newsEntity = newsRepository.findById(newsUpdatePortCommand.id())
                .orElseThrow(() -> NewsNotFoundException.byId(newsUpdatePortCommand.id()));

        newsEntity.setId(newsUpdatePortCommand.id());
        newsEntity.setText(newsUpdatePortCommand.text());
        newsEntity.setTitle(newsUpdatePortCommand.title());
        newsEntity.setTime(newsUpdatePortCommand.time());

        return newsMapper.toNewsPortResult(newsEntity);
    }

    /**
     * Удаляет новость из базы данных по её ID.
     *
     * @param newsId ID новости.
     */
    @Override
    public void deleteNewsById(UUID newsId) {
        newsRepository.deleteById(newsId);
    }
}
