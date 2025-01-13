package ru.clevertec.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.domain.News;
import ru.clevertec.port.input.news.command.NewsCreateCommand;
import ru.clevertec.port.input.news.command.NewsUpdateCommand;
import ru.clevertec.port.input.news.command.NewsUseCaseResult;
import ru.clevertec.port.output.port.NewsCreatePortCommand;
import ru.clevertec.port.output.port.NewsPortResult;
import ru.clevertec.port.output.port.NewsUpdatePortCommand;

/**
 * Mapper интерфейс для преобразования объектов News между различными представлениями (команды, домены, порты-команды).
 * Использует MapStruct для автоматической генерации кода маппера.
 */
@Mapper(componentModel = "spring")
public interface DomainNewsMapper {

    /**
     * Преобразует {@link NewsPortResult} в {@link NewsUseCaseResult}.
     *
     * @param newsPortResult Объект NewsPortResult.
     * @return Объект NewsUseCaseResult.
     */
    NewsUseCaseResult toNewsUseCaseResult(NewsPortResult newsPortResult);

    /**
     * Преобразует {@link News} в {@link NewsUseCaseResult}.
     *
     * @param news Объект News.
     * @return Объект NewsUseCaseResult.
     */
    NewsUseCaseResult toNewsUseCaseResult(News news);

    /**
     * Преобразует {@link NewsCreateCommand} в {@link News}.
     *
     * @param newsCreateCommand Объект NewsCreateCommand.
     * @return Объект News.
     */
    News toNews(NewsCreateCommand newsCreateCommand);

    /**
     * Преобразует {@link NewsUpdateCommand} в {@link News}.
     *
     * @param newsUpdateCommand Объект NewsUpdateCommand.
     * @return Объект News.
     */
    News toNews(NewsUpdateCommand newsUpdateCommand);

    /**
     * Преобразует {@link NewsPortResult} в {@link News}.
     *
     * @param newsPortResult Объект NewsPortResult.
     * @return Объект News.
     */
    News toNews(NewsPortResult newsPortResult);

    /**
     * Преобразует {@link News} в {@link NewsCreatePortCommand}.
     *
     * @param news Объект News.
     * @return Объект NewsCreatePortCommand.
     */
    NewsCreatePortCommand toNewsCreatePortCommand(News news);

    /**
     * Преобразует {@link News} в {@link NewsUpdatePortCommand}.
     *
     * @param news Объект News.
     * @return Объект NewsUpdatePortCommand.
     */
    NewsUpdatePortCommand toNewsUpdatePortCommand(News news);
}
