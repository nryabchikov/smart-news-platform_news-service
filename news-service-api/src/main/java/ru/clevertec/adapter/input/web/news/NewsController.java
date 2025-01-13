package ru.clevertec.adapter.input.web.news;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.adapter.input.web.news.dto.NewsRequest;
import ru.clevertec.adapter.input.web.news.dto.NewsResponse;
import ru.clevertec.adapter.input.web.news.dto.NewsUpdateRequest;
import ru.clevertec.port.input.news.CreateNewsUseCase;
import ru.clevertec.port.input.news.DeleteNewsUseCase;
import ru.clevertec.port.input.news.ReadNewsUseCase;
import ru.clevertec.port.input.news.UpdateNewsUseCase;
import ru.clevertec.port.input.news.command.NewsCreateCommand;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
/**
 * REST контроллер для управления новостями.
 * Предоставляет эндпоинты для получения, создания, обновления и удаления новостей.
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/news")
public class NewsController {

    /**
     * Сервис для создания новостей.
     */
    private final CreateNewsUseCase createNewsUseCase;

    /**
     * Сервис для получения новостей.
     */
    private final ReadNewsUseCase readNewsUseCase;

    /**
     * Сервис для обновления новостей.
     */
    private final UpdateNewsUseCase updateNewsUseCase;

    /**
     * Сервис для удаления новостей.
     */
    private final DeleteNewsUseCase deleteNewsUseCase;

    /**
     * Маппер для преобразования DTO в объекты команды и обратно.
     */
    private final WebNewsMapper newsMapper;


    /**
     * Возвращает список всех новостей используя пагинацию.
     *
     * @param pageNumber Номер страницы. Значение по умолчанию: 0.
     * @param pageSize   Размер страницы. Значение по умолчанию: 3.
     * @return Список новостей {@link NewsResponse}.
     */

    @Operation(
            parameters = {
                    @Parameter(name = "pageNumber", description = "Номер страницы"),
                    @Parameter(name = "pageSize", description = "Размер страницы")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = NewsResponse.class
                                            )
                                    )
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<NewsResponse>> readAllNews(
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "3") int pageSize) {

        List<NewsResponse> newsResponses = readNewsUseCase.readAllNews(pageNumber, pageSize)
                .map(newsMapper::toNewsResponse)
                .getContent();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(newsResponses);
    }

    /**
     * Возвращает новость по её ID.
     *
     * @param newsId ID новости.
     * @return Новость {@link NewsResponse}.
     */
    @Operation(
            parameters = {
                    @Parameter(
                            name = "newsId", description = "ID новости",
                            schema = @Schema(type = "string", format = "uuid"))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = NewsResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Новость не найдена")
            }
    )
    @GetMapping("/{newsId}")
    public ResponseEntity<NewsResponse> readNewsById(@PathVariable("newsId") @Valid @NotNull UUID newsId) {

        NewsResponse newsResponse = newsMapper.toNewsResponse(readNewsUseCase.readNewsById(newsId));

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(newsResponse);
    }

    /**
     * Создает новую новость.
     *
     * @param newsRequest Запрос на создание новости.
     * @param principal   Авторизованный пользователь.
     * @return Созданная новость {@link NewsResponse}.
     */

    @Operation(
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    type = "object",
                                    example = """
                                            {
                                              "title": "Захватывающий заголовок новости!",
                                              "text": "Супер-пупер мега крутая новость!",
                                              "time": "2023-10-18T15:30:00"
                                            }
                                            """,
                                    properties = {
                                            @StringToClassMapItem(key = "title", value = String.class),
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
                                                            @StringToClassMapItem(key = "title", value = String.class),
                                                            @StringToClassMapItem(key = "text", value = String.class),
                                                            @StringToClassMapItem(key = "time", value = LocalDateTime.class),
                                                            @StringToClassMapItem(key = "authorId", value = UUID.class)
                                                    }
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(responseCode = "400", description = "Плохой запрос"),
                    @ApiResponse(responseCode = "422", description = "Ошибки валидации сущности"),
            })
    @PostMapping
    public ResponseEntity<NewsResponse> createNews(@RequestBody @Valid NewsRequest newsRequest,
                                                   Principal principal) {

        NewsCreateCommand newsCreateCommand = newsMapper.toNewsCreateCommand(newsRequest);
        NewsResponse newsResponse = newsMapper.toNewsResponse(
                createNewsUseCase.createNews(newsCreateCommand, UUID.fromString(principal.getName()))
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(newsResponse);
    }

    /**
     * Обновляет существующую новость.
     *
     * @param newsRequest Запрос на обновление новости.
     * @return Обновленная новость {@link NewsResponse}.
     */
    @Operation(
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    type = "object",
                                    example = """
                                        {
                                          "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
                                          "title": "Обновленный не такой уже заголовок",
                                          "text": "Обновленный и скушный текст новости",
                                          "time": "2025-01-10T11:00:00"
                                        }
                                        """,
                                    properties = {
                                            @StringToClassMapItem(key = "id", value = UUID.class),
                                            @StringToClassMapItem(key = "title", value = String.class),
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
                                    schema = @Schema(implementation = NewsResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Новость не найдена"),
                    @ApiResponse(responseCode = "400", description = "Плохой запрос"),
                    @ApiResponse(responseCode = "422", description = "Ошибки валидации сущности"),
            }
    )
    @PutMapping
    public ResponseEntity<NewsResponse> updateNews(@RequestBody @Valid NewsUpdateRequest newsRequest) {

        NewsResponse newsResponse = newsMapper.toNewsResponse(
                updateNewsUseCase.updateNews(newsMapper.toNewsUpdateCommand(newsRequest))
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(newsResponse);
    }

    /**
     * Удаляет новость по её ID.
     *
     * @param id ID новости.
     * @return Ответ с кодом 204 (No Content).
     */
    @Operation(
            parameters = {
                    @Parameter(
                            name = "newsId",
                            description = "ID новости",
                            schema = @Schema(type = "string", format = "uuid")
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Новость успешно удалена")
            }
    )
    @DeleteMapping("/{newsId}")
    public ResponseEntity<Void> deleteById(@PathVariable("newsId") @Valid @NotNull UUID id) {

        deleteNewsUseCase.deleteNews(id);
        return ResponseEntity.noContent()
                .build();
    }
}
