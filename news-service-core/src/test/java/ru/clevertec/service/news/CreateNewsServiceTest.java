package ru.clevertec.service.news;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.cache.Cache;
import ru.clevertec.client.user.UserClient;
import ru.clevertec.client.user.dto.UserResponse;
import ru.clevertec.domain.News;
import ru.clevertec.mapper.DomainNewsMapper;
import ru.clevertec.port.input.news.command.NewsCreateCommand;
import ru.clevertec.port.input.news.command.NewsUseCaseResult;
import ru.clevertec.port.output.WriteNewsPort;
import ru.clevertec.port.output.port.NewsCreatePortCommand;
import ru.clevertec.port.output.port.NewsPortResult;
import ru.clevertec.util.news.TestData;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateNewsServiceTest {

    @Mock
    Cache<UUID, News> cache;

    @Mock
    WriteNewsPort newsPort;

    @Mock
    DomainNewsMapper newsMapper;

    @Mock
    UserClient userClient;

    @InjectMocks
    CreateNewsService createNewsService;

    @Test
    void shouldCreateNews() {
        //given
        UUID keycloakUserId = UUID.randomUUID();
        News news = TestData.generateNews();
        NewsCreateCommand newsCreateCommand = TestData.generateNewsCreateCommand();
        NewsCreatePortCommand newsCreatePortCommand = TestData.generateNewsCreatePortCommand();
        NewsPortResult newsPortResult = TestData.generateNewsPortResult();
        NewsUseCaseResult newsUseCaseResult = TestData.generateNewsUseCaseResult();
        UserResponse userResponse = new UserResponse(keycloakUserId, UUID.randomUUID(), "Nekitos4");

        when(userClient.readUserByKeycloakId(keycloakUserId))
                .thenReturn(userResponse);
        when(newsMapper.toNews(newsCreateCommand))
                .thenReturn(news);
        when(newsMapper.toNewsCreatePortCommand(news))
                .thenReturn(newsCreatePortCommand);
        when(newsPort.createNews(newsCreatePortCommand))
                .thenReturn(newsPortResult);
        when(newsMapper.toNewsUseCaseResult(newsPortResult))
                .thenReturn(newsUseCaseResult);

        //when
        NewsUseCaseResult actualNewsUseCaseResult = createNewsService.createNews(newsCreateCommand, keycloakUserId);

        //then
        assertEquals(newsUseCaseResult, actualNewsUseCaseResult);
        verify(userClient).readUserByKeycloakId(any());
        verify(newsMapper).toNewsCreatePortCommand(any());
        verify(newsPort).createNews(any());
        verify(newsMapper).toNewsUseCaseResult(any(NewsPortResult.class));
        verify(cache).put(any(), any());
    }
}