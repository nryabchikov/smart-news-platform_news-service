package ru.clevertec.adapter.output.persistence;

import org.mapstruct.Mapper;
import ru.clevertec.adapter.output.persistence.jpa.entity.NewsEntity;
import ru.clevertec.port.output.port.NewsCreatePortCommand;
import ru.clevertec.port.output.port.NewsPortResult;

/**
 * Mapper интерфейс для преобразования объектов NewsEntity в NewsPort и обратно.
 * Использует MapStruct для автоматической генерации кода маппера.
 */
@Mapper(componentModel = "spring")
public interface PersistenceNewsMapper {

    /**
     * Преобразует объект NewsCreatePortCommand в объект NewsEntity.
     *
     * @param newsCreatePortCommand Объект NewsCreatePortCommand.
     * @return Объект NewsEntity.
     */
    NewsEntity toNewsEntity(NewsCreatePortCommand newsCreatePortCommand);

    /**
     * Преобразует объект NewsEntity в объект NewsPortResult.
     *
     * @param newsEntity Объект NewsEntity.
     * @return Объект NewsPortResult.
     */
    NewsPortResult toNewsPortResult(NewsEntity newsEntity);
}
