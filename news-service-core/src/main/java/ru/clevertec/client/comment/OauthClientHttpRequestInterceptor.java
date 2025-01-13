package ru.clevertec.client.comment;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

/**
 * Interceptor для добавления токена доступа OAuth2 в заголовок Authorization запросов Feign клиента.
 */
@Slf4j
@RequiredArgsConstructor
public class OauthClientHttpRequestInterceptor implements RequestInterceptor {

    /** Менеджер авторизованных клиентов OAuth2. */
    private final OAuth2AuthorizedClientManager authorizedClientManager;

    /** ID регистрации OAuth2 клиента. */
    private final String registrationId;

    /**
     * Добавляет токен доступа OAuth2 в заголовок Authorization запроса, если пользователь аутентифицирован.
     * @param template Объект RequestTemplate.
     */
    @Override
    public void apply(RequestTemplate template) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !template.headers().containsKey("Authorization")) {
            log.debug("OauthClientHttpRequestInterceptor.apply: Adding Authorization header.");
            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId(registrationId)
                    .principal(authentication)
                    .build();
            OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);
            template.header("Authorization", "Bearer " + authorizedClient.getAccessToken().getTokenValue());
            log.debug("OauthClientHttpRequestInterceptor.apply: Authorization header added successfully.");
        } else {
            log.debug("OauthClientHttpRequestInterceptor.apply: User not authenticated or Authorization header already present.");
        }
    }
}
