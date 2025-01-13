package ru.clevertec.adapter.input.web.comment;

import org.mapstruct.Mapper;
import ru.clevertec.client.comment.dto.CommentRequest;
import ru.clevertec.client.comment.dto.CommentResponse;
import ru.clevertec.client.comment.dto.CommentUpdateRequest;
import ru.clevertec.port.input.comment.command.CommentCreateCommand;
import ru.clevertec.port.input.comment.command.CommentUpdateCommand;
import ru.clevertec.port.input.comment.command.CommentUseCaseResult;

/**
 * Mapper интерфейс для преобразования DTO объектов Comment в объекты команды и обратно.
 * Использует MapStruct для автоматической генерации кода маппера.
 */
@Mapper(componentModel = "spring")
public interface WebCommentMapper {

    /**
     * Преобразует объект CommentRequest в объект CommentCreateCommand.
     *
     * @param commentRequest Объект CommentRequest.
     * @return Объект CommentCreateCommand.
     */
    CommentCreateCommand toCommentCreateCommand(CommentRequest commentRequest);

    /**
     * Преобразует объект CommentUpdateRequest в объект CommentUpdateCommand.
     *
     * @param commentUpdateRequest Объект CommentUpdateRequest.
     * @return Объект CommentUpdateCommand.
     */
    CommentUpdateCommand toCommentUpdateCommand(CommentUpdateRequest commentUpdateRequest);

    /**
     * Преобразует объект CommentUseCaseResult в объект CommentResponse.
     *
     * @param commentUseCaseResult Объект CommentUseCaseResult.
     * @return Объект CommentResponse.
     */
    CommentResponse toCommentResponse(CommentUseCaseResult commentUseCaseResult);
}
