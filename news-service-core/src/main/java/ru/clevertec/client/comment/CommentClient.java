package ru.clevertec.client.comment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.clevertec.client.comment.dto.CommentRequest;
import ru.clevertec.client.comment.dto.CommentResponse;
import ru.clevertec.client.comment.dto.CommentUpdateRequest;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

/**
 * Клиент Feign для взаимодействия с сервисом комментариев.
 */
@FeignClient(name = "comment-service", url = "${comment.service.url}", configuration = RequestO2Interceptor.class)
public interface CommentClient {

    /**
     * Возвращает список всех комментариев к указанной новости, используя пагинацию.
     *
     * @param newsId     ID новости.
     * @param pageNumber Номер страницы.
     * @param pageSize   Размер страницы.
     * @return Список комментариев {@link CommentResponse}.
     */
    @GetMapping("/api/v1/news/{newsId}/comments")
    List<CommentResponse> readAllComments(
            @PathVariable("newsId") UUID newsId,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "3") int pageSize);

    /**
     * Возвращает комментарий по его ID и по ID новости.
     *
     * @param newsId    ID новости.
     * @param commentId ID комментария.
     * @return Комментарий {@link CommentResponse}.
     */
    @GetMapping("/api/v1/news/{newsId}/comments/{commentId}")
    CommentResponse readCommentById(@PathVariable("newsId") UUID newsId,
                                    @PathVariable("commentId") UUID commentId);

    /**
     * Создает новый комментарий к указанной новости.
     *
     * @param newsId         ID новости.
     * @param commentRequest Запрос на создание комментария.
     * @return Созданный комментарий {@link CommentResponse}.
     */
    @PostMapping("/api/v1/news/{newsId}/comments")
    CommentResponse createComment(@PathVariable("newsId") UUID newsId,
                                  @RequestBody CommentRequest commentRequest);

    /**
     * Обновляет существующий комментарий к указанной новости.
     *
     * @param newsId         ID новости.
     * @param commentRequest Запрос на обновление комментария.
     * @return Обновленный комментарий {@link CommentResponse}.
     */
    @PutMapping("/api/v1/news/{newsId}/comments")
    CommentResponse updateComment(@PathVariable("newsId") UUID newsId,
                                  @RequestBody CommentUpdateRequest commentRequest);

    /**
     * Удаляет комментарий по его ID и по ID новости.
     *
     * @param newsId    ID новости.
     * @param commentId ID комментария.
     */
    @DeleteMapping("/api/v1/news/{newsId}/comments/{commentId}")
    void deleteCommentById(@PathVariable("newsId") UUID newsId,
                    @PathVariable("commentId") UUID commentId);

    @DeleteMapping("/api/v1/news/{newsId}/comments")
    void deleteAllComments(@PathVariable("newsId") UUID newsId);
}
