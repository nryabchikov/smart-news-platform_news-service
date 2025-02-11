package ru.clevertec.adapter.input.web.news;

import org.mapstruct.Mapper;
import ru.clevertec.adapter.input.web.news.dto.NewsRequest;
import ru.clevertec.adapter.input.web.news.dto.NewsResponse;
import ru.clevertec.adapter.input.web.news.dto.NewsUpdateRequest;
import ru.clevertec.port.input.news.command.NewsCreateCommand;
import ru.clevertec.port.input.news.command.NewsUpdateCommand;
import ru.clevertec.port.input.news.command.NewsUseCaseResult;

/**
 * Mapper интерфейс для преобразования DTO объектов News в объекты команды и обратно.
 * Использует MapStruct для автоматической генерации кода маппера.
 */
@Mapper(componentModel = "spring")
public interface WebNewsMapper {

    /**
     * Преобразует объект NewsRequest в объект NewsCreateCommand.
     * @param newsRequest Объект NewsRequest.
     * @return Объект NewsCreateCommand.
     */
    NewsCreateCommand toNewsCreateCommand(NewsRequest newsRequest);

    /**
     * Преобразует объект NewsUpdateRequest в объект NewsUpdateCommand.
     * @param newsUpdateRequest Объект NewsUpdateRequest.
     * @return Объект NewsUpdateCommand.
     */
    NewsUpdateCommand toNewsUpdateCommand(NewsUpdateRequest newsUpdateRequest);

    /**
     * Преобразует объект NewsUseCaseResult в объект NewsResponse.
     * @param newsUseCaseResult Объект NewsUseCaseResult.
     * @return Объект NewsResponse.
     */
    NewsResponse toNewsResponse(NewsUseCaseResult newsUseCaseResult);
}
