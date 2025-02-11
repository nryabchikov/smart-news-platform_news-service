package ru.clevertec.adapter.input.web.news;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.exception.NewsNotFoundException;

/**
 * Глобальный обработчик исключений для контроллера новостей.
 * Перехватывает различные типы исключений и возвращает соответствующие ответы клиенту в формате JSON.
 */
@Slf4j
@RestControllerAdvice
public class NewsControllerAdvice {

    /**
     * Обработчик всевозможных исключений(Exception). Возвращает ответ с кодом 400 (Bad Request).
     *
     * @param exception Исключение, которое было перехвачено.
     * @return Ответ с кодом 400, содержащий информацию об исключении.
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionObject> response400(@RequestBody Exception exception) {
        log.error("NewsControllerAdvice.response400: Unexpected exception caught.", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(aggregate(exception.getMessage(), HttpStatus.BAD_REQUEST));
    }

    /**
     * Обработчик исключений валидации. Возвращает ответ с кодом 422 (Unprocessable Entity).
     *
     * @param exception Исключение валидации.
     * @return Ответ с кодом 422, содержащий информацию об исключении.
     */
    @ExceptionHandler(value = {
            ValidationException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ExceptionObject> response422(@RequestBody Exception exception) {
        log.error("NewsControllerAdvice.response422: Validation exception caught.", exception);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(aggregate(exception.getLocalizedMessage(), HttpStatus.UNPROCESSABLE_ENTITY));
    }

    /**
     * Обработчик исключения NewsNotFoundException. Возвращает ответ с кодом 404 (Not Found).
     *
     * @param exception Исключение NewsNotFoundException.
     * @return Ответ с кодом 404, содержащий информацию об исключении.
     */
    @ExceptionHandler(value = {
            NewsNotFoundException.class
    })
    public ResponseEntity<ExceptionObject> response404(@RequestBody Exception exception) {
        log.error("NewsControllerAdvice.response404: NewsNotFoundException caught.", exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(aggregate(exception.getMessage(), HttpStatus.NOT_FOUND));
    }

    /**
     * Вспомогательный метод для создания объекта ExceptionObject.
     *
     * @param message    Сообщение об ошибке.
     * @param httpStatus HTTP статус.
     * @return Объект ExceptionObject.
     */
    private ExceptionObject aggregate(String message, HttpStatus httpStatus) {
        return new ExceptionObject(httpStatus.value(), httpStatus.name(), message);
    }

    /**
     * Класс для представления информации об исключении.
     */
    public record ExceptionObject(int code, String status, String message) {
    }
}
