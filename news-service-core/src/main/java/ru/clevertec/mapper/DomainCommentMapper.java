package ru.clevertec.mapper;

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
public interface DomainCommentMapper {

    /**
     * Преобразует {@link CommentCreateCommand} в {@link CommentRequest}.
     *
     * @param commentCreateCommand Объект CommentCreateCommand.
     * @return Объект CommentRequest.
     */
    CommentRequest toCommentRequest(CommentCreateCommand commentCreateCommand);

    /**
     * Преобразует {@link CommentUpdateCommand} в {@link CommentUpdateRequest}.
     *
     * @param commentUpdateCommand Объект CommentUpdateCommand.
     * @return Объект CommentUpdateRequest.
     */
    CommentUpdateRequest toCommentUpdateRequest(CommentUpdateCommand commentUpdateCommand);

    /**
     * Преобразует {@link CommentResponse} в {@link CommentUseCaseResult}.
     *
     * @param commentResponse Объект CommentResponse.
     * @return Объект CommentUseCaseResult.
     */
    CommentUseCaseResult toCommentUseCaseResult(CommentResponse commentResponse);
}



