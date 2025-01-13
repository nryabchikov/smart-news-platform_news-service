package ru.clevertec.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Конфигурационный класс для настройки Spring Security.
 */
@Slf4j
@Configuration
public class SecurityConfig {

    /**
     * Обработчик успешной аутентификации.
     */
    @Autowired
    private NewsAuthenticationSuccessHandler newsAuthenticationSuccessHandler;

//    @Autowired
//    private NewsAccessFilter newsAccessFilter;

    /**
     * Настраивает SecurityFilterChain для управления доступом к ресурсам приложения.
     *
     * @param http Объект HttpSecurity.
     * @return SecurityFilterChain.
     * @throws Exception Если возникла ошибка во время настройки.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.GET, "/api/v1/news").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/news/{newsId}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/news")
                        .hasAnyRole("ADMIN", "JOURNALIST")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/news")
                        .hasAnyRole("ADMIN", "JOURNALIST")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/news/{newsId}")
                        .hasAnyRole("ADMIN", "JOURNALIST")
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2.successHandler(newsAuthenticationSuccessHandler))
                .oauth2Client(Customizer.withDefaults())
                //.addFilterBefore(newsAccessFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Настраивает OAuth2UserService для обработки информации о пользователе(получение пользовательских authorities)
     * после успешной аутентификации.
     *
     * @return OAuth2UserService.
     */
    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService() {
        log.info("SecurityConfig.oAuth2UserService: Configuring OAuth2UserService");
        OidcUserService oidcUserService = new OidcUserService();
        OAuth2UserService<OidcUserRequest, OidcUser> userService = userRequest -> {
            OidcUser oidcUser = oidcUserService.loadUser(userRequest);
            log.debug("SecurityConfig.oAuth2UserService: Loaded OidcUser: {}", oidcUser);
            List<GrantedAuthority> authorities =
                    Stream.concat(oidcUser.getAuthorities().stream(),
                                    Optional.ofNullable(oidcUser.getClaimAsStringList("groups"))
                                            .orElseGet(List::of)
                                            .stream()
                                            .filter(role -> role.startsWith("ROLE_"))
                                            .map(SimpleGrantedAuthority::new)
                                            .map(GrantedAuthority.class::cast))
                            .toList();
            log.debug("SecurityConfig.oAuth2UserService: Authorities extracted: {}", authorities);
            return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
        };
        log.info("SecurityConfig.oAuth2UserService: OAuth2UserService configured successfully.");
        return userService;
    }
}
