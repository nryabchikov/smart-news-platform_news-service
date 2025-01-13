package ru.clevertec.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.clevertec.client.user.UserClient;
import ru.clevertec.client.user.dto.UserRequest;

import java.util.UUID;

/**
 * Обработчик успешной аутентификации, который создает или обновляет пользователя в системе
 * после успешной аутентификации c помощью Keycloak.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NewsAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * Клиент для работы с сервисом пользователей.
     */
    private final UserClient userClient;

    /**
     * Выполняется после успешной аутентификации.
     * Извлекает информацию о пользователе из Keycloak и создает или обновляет пользователя в системе.
     *
     * @param request        HTTP запрос.
     * @param response       HTTP ответ.
     * @param authentication Объект аутентификации.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String keycloakUserId = oidcUser.getAttribute("sub");
        String username = oidcUser.getAttribute("preferred_username");
        log.info("NewsAuthenticationSuccessHandler.onAuthenticationSuccess: Authentication successful." +
                "  keycloakUserId={}, username={}", keycloakUserId, username);

        try {
            UserRequest userRequest = UserRequest.builder()
                    .keycloakUserId(UUID.fromString(keycloakUserId))
                    .username(username)
                    .build();
            log.debug("NewsAuthenticationSuccessHandler.onAuthenticationSuccess:" +
                    " Calling userClient.createOrUpdateUser with request: {}", userRequest);
            userClient.createOrUpdateUser(userRequest);

            log.debug("NewsAuthenticationSuccessHandler.onAuthenticationSuccess: User created or updated successfully.");
            response.sendRedirect("/swagger-ui/index.html");
        } catch (Exception e) {
            log.error("NewsAuthenticationSuccessHandler.onAuthenticationSuccess: Error during user creation or update.", e);
        }
    }
}

