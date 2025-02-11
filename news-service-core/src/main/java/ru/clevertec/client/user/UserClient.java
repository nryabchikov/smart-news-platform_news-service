package ru.clevertec.client.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.clevertec.client.user.dto.UserRequest;
import ru.clevertec.client.user.dto.UserResponse;

import java.util.UUID;

/**
 * Feign клиент для взаимодействия с сервисом пользователей.
 */
@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserClient {

    /**
     * Возвращает информацию о пользователе по его Keycloak ID.
     *
     * @param keycloakId Keycloak ID пользователя.
     * @return Информация о пользователе {@link UserResponse}.
     */
    @GetMapping("api/v1/users/{keycloakId}")
    UserResponse readUserByKeycloakId(@PathVariable("keycloakId") UUID keycloakId);

    /**
     * Создает или обновляет информацию о пользователе.
     *
     * @param userRequest Запрос на создание или обновление пользователя.
     * @return Информация о пользователе {@link UserResponse}.
     */
    @PostMapping("api/v1/users")
    UserResponse createOrUpdateUser(@RequestBody UserRequest userRequest);
}


