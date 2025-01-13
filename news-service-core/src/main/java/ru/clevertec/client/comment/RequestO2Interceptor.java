package ru.clevertec.client.comment;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;

/**
 * Конфигурационный класс для создания бина RequestInterceptor для Feign клиента.
 */
@Slf4j
@Configuration
public class RequestO2Interceptor {

    /**
     * Создает и возвращает бины {@link OauthClientHttpRequestInterceptor}.
     *
     * @param clientRegistrationRepository Репозиторий для регистрации клиентов OAuth2.
     * @param authorizedClientRepository Репозиторий для авторизованных клиентов OAuth2.
     * @param registrationId ID регистрации OAuth2 клиента.
     * @return {@link RequestInterceptor}
     */
    @Bean
    public RequestInterceptor oauthClientHttpRequestInterceptor(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository,
            @Value("${comment.service.registration-id:keycloak}") String registrationId
    ) {
        log.info("RequestO2Interceptor.oauthClientHttpRequestInterceptor: Creating OAuth2 interceptor.");
        RequestInterceptor interceptor = new OauthClientHttpRequestInterceptor(
                new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository),
                registrationId);
        log.info("RequestO2Interceptor.oauthClientHttpRequestInterceptor: OAuth2 interceptor created successfully.");
        return interceptor;
    }
}
