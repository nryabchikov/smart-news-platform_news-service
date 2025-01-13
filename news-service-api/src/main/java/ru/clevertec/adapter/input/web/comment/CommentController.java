package ru.clevertec.adapter.input.web.comment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.adapter.input.web.news.dto.NewsResponse;
import ru.clevertec.client.comment.dto.CommentRequest;
import ru.clevertec.client.comment.dto.CommentResponse;
import ru.clevertec.client.comment.dto.CommentUpdateRequest;
import ru.clevertec.port.input.comment.CreateCommentUseCase;
import ru.clevertec.port.input.comment.DeleteCommentUseCase;
import ru.clevertec.port.input.comment.ReadCommentUseCase;
import ru.clevertec.port.input.comment.UpdateCommentUseCase;
import ru.clevertec.port.input.news.ReadNewsUseCase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * REST контроллер для управления комментариями к новостям.
 * Предоставляет эндпоинты для получения, создания, обновления и удаления комментариев.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/news/{newsId}/comments")
public class CommentController {

    /**
     * Сервис для создания комментариев.
     */
    private final CreateCommentUseCase createCommentUseCase;

    /**
     * Сервис для получения комментариев.
     */
    private final ReadCommentUseCase readCommentUseCase;

    /**
     * Сервис для обновления комментариев.
     */
    private final UpdateCommentUseCase updateCommentUseCase;

    /**
     * Сервис для удаления комментариев.
     */
    private final DeleteCommentUseCase deleteCommentUseCase;

    /**
     * Сервис для получения новостей.
     */
    private final ReadNewsUseCase readNewsUseCase;

    /**
     * Маппер для преобразования DTO в объекты команды и обратно.
     */
    private final WebCommentMapper commentMapper;

    /**
     * Возвращает список всех комментариев к указанной новости, используя пагинацию.
     *
     * @param newsId     ID новости.
     * @param pageNumber Номер страницы. Значение по умолчанию: 0.
     * @param pageSize   Размер страницы. Значение по умолчанию: 3.
     * @return Список комментариев {@link CommentResponse}.
     */
    @Operation(
            parameters = {
                    @Parameter(name = "pageNumber", description = "Номер страницы"),
                    @Parameter(name = "pageSize", description = "Размер страницы"),
                    @Parameter(
                            name = "newsId", description = "ID новости",
                            schema = @Schema(type = "string", format = "uuid")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = CommentResponse.class
                                            )
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Новость не найдена")
            }
    )
    @GetMapping
    public ResponseEntity<List<CommentResponse>> readAllComments(
            @PathVariable("newsId") UUID newsId,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "3") int pageSize) {

        readNewsUseCase.readNewsById(newsId);
        List<CommentResponse> commentResponses =
                readCommentUseCase.readAllCommentsByNewsId(newsId, pageNumber, pageSize).stream()
                        .map(commentMapper::toCommentResponse)
                        .toList();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(commentResponses);
    }

    /**
     * Возвращает комментарий по его ID и по ID новости.
     *
     * @param newsId    ID новости.
     * @param commentId ID комментария.
     * @return Комментарий {@link CommentResponse}.
     */
    @Operation(
            parameters = {
                    @Parameter(
                            name = "newsId", description = "ID новости",
                            schema = @Schema(type = "string", format = "uuid")
                    ),
                    @Parameter(
                            name = "commentId", description = "ID комментария",
                            schema = @Schema(type = "string", format = "uuid")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = CommentResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Новость не найдена"),
                    @ApiResponse(responseCode = "404", description = "Комментарий не найден")
            }
    )
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> readCommentById(@PathVariable("newsId") UUID newsId,
                                                           @PathVariable("commentId") UUID commentId) {

        readNewsUseCase.readNewsById(newsId);
        CommentResponse commentResponse = commentMapper.toCommentResponse(
                readCommentUseCase.readCommentByIdAndNewsId(commentId, newsId)
        );

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(commentResponse);
    }

    /**
     * Создает новый комментарий к указанной новости.
     *
     * @param newsId         ID новости.
     * @param commentRequest Запрос на создание комментария.
     * @return Созданный комментарий {@link CommentResponse}.
     */
    @Operation(
            parameters = {
                    @Parameter(
                            name = "newsId", description = "ID новости",
                            schema = @Schema(type = "string", format = "uuid"))
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    type = "object",
                                    example = """
                                            {
                                              "text": "Супер-пупер мега крутой комментарий!",
                                              "time": "2023-10-18T15:30:00"
                                            }
                                            """,
                                    properties = {
                                            @StringToClassMapItem(key = "text", value = String.class),
                                            @StringToClassMapItem(key = "time", value = LocalDateTime.class)
                                    }
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            headers = @Header(name = "Content-Type", description = "Data type"),
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(
                                                    type = "object",
                                                    properties = {
                                                            @StringToClassMapItem(key = "id", value = UUID.class),
                                                            @StringToClassMapItem(key = "text", value = String.class),
                                                            @StringToClassMapItem(key = "time", value = LocalDateTime.class),
                                                            @StringToClassMapItem(key = "authorId", value = UUID.class)
                                                    }
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(responseCode = "404", description = "Новость не найдена"),
                    @ApiResponse(responseCode = "400", description = "Плохой запрос"),
                    @ApiResponse(responseCode = "422", description = "Ошибки валидации сущности"),
            })
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@PathVariable("newsId") UUID newsId,
                                                         @RequestBody CommentRequest commentRequest) {

        readNewsUseCase.readNewsById(newsId);
        CommentResponse commentResponse = commentMapper.toCommentResponse(
                createCommentUseCase.createComment(newsId, commentMapper.toCommentCreateCommand(commentRequest))
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(commentResponse);
    }

    /**
     * Обновляет существующий комментарий к указанной новости.
     *
     * @param newsId               ID новости.
     * @param commentUpdateRequest Запрос на обновление комментария.
     * @return Обновленный комментарий {@link CommentResponse}.
     */
    @Operation(
            parameters = {
                    @Parameter(
                            name = "newsId", description = "ID новости",
                            schema = @Schema(type = "string", format = "uuid"))
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    type = "object",
                                    example = """
                                        {
                                          "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
                                          "text": "Обновленный и скушный текст комментария",
                                          "time": "2025-01-10T11:00:00"
                                        }
                                        """,
                                    properties = {
                                            @StringToClassMapItem(key = "id", value = UUID.class),
                                            @StringToClassMapItem(key = "text", value = String.class),
                                            @StringToClassMapItem(key = "time", value = LocalDateTime.class)
                                    }
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Новость не найдена"),
                    @ApiResponse(responseCode = "404", description = "Комментарий не найден"),
                    @ApiResponse(responseCode = "400", description = "Плохой запрос"),
                    @ApiResponse(responseCode = "422", description = "Ошибки валидации сущности"),
            }
    )
    @PutMapping
    public ResponseEntity<CommentResponse> updateComment(@PathVariable("newsId") UUID newsId,
                                                         @RequestBody CommentUpdateRequest commentUpdateRequest) {

        readNewsUseCase.readNewsById(newsId);
        CommentResponse commentResponse = commentMapper.toCommentResponse(
                updateCommentUseCase.updateComment(newsId, commentMapper.toCommentUpdateCommand(commentUpdateRequest))
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(commentResponse);
    }

    /**
     * Удаляет комментарий по его ID и по ID новости.
     *
     * @param newsId    ID новости.
     * @param commentId ID комментария.
     * @return Ответ с кодом 204 (No Content).
     */
    @Operation(
            parameters = {
                    @Parameter(
                            name = "newsId",
                            description = "ID новости",
                            schema = @Schema(type = "string", format = "uuid")
                    ),
                    @Parameter(
                            name = "commentId",
                            description = "ID комментария",
                            schema = @Schema(type = "string", format = "uuid")
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Комментарий успешно удален"),
                    @ApiResponse(responseCode = "404", description = "Новость не найдена")
            }
    )
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteById(@PathVariable("newsId") UUID newsId,
                                           @PathVariable("commentId") UUID commentId) {

        readNewsUseCase.readNewsById(newsId);
        deleteCommentUseCase.deleteComment(newsId, commentId);

        return ResponseEntity.noContent()
                .build();
    }
}
