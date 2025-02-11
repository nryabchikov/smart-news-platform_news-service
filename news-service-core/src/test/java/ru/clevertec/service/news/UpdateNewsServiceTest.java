package ru.clevertec.service.news;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.cache.Cache;
import ru.clevertec.domain.News;
import ru.clevertec.mapper.DomainNewsMapper;
import ru.clevertec.port.input.news.command.NewsUpdateCommand;
import ru.clevertec.port.input.news.command.NewsUseCaseResult;
import ru.clevertec.port.output.WriteNewsPort;
import ru.clevertec.port.output.port.NewsPortResult;
import ru.clevertec.port.output.port.NewsUpdatePortCommand;
import ru.clevertec.util.news.TestData;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateNewsServiceTest {

    @Mock
    Cache<UUID, News> cache;

    @Mock
    WriteNewsPort newsPort;

    @Mock
    DomainNewsMapper newsMapper;

    @InjectMocks
    UpdateNewsService updateNewsService;

    @Test
    void shouldUpdateNews() {
        //given
        News news = TestData.generateNews();
        NewsUpdateCommand newsUpdateCommand = TestData.generateNewsUpdateCommand();
        NewsUpdatePortCommand newsUpdatePortCommand = TestData.generateNewsUpdatePortCommand();
        NewsPortResult newsPortResult = TestData.generateNewsPortResult();
        NewsUseCaseResult newsUseCaseResult = TestData.generateNewsUseCaseResult();

        when(newsMapper.toNews(newsUpdateCommand))
                .thenReturn(news);
        when(newsMapper.toNewsUpdatePortCommand(news))
                .thenReturn(newsUpdatePortCommand);
        when(newsPort.updateNews(newsUpdatePortCommand))
                .thenReturn(newsPortResult);
        when(newsMapper.toNewsUseCaseResult(newsPortResult))
                .thenReturn(newsUseCaseResult);

        //when
        NewsUseCaseResult actualNewsUseCaseResult = updateNewsService.updateNews(newsUpdateCommand);

        //then
        assertEquals(newsUseCaseResult, actualNewsUseCaseResult);
        verify(newsMapper).toNews(any(NewsUpdateCommand.class));
        verify(newsMapper).toNewsUpdatePortCommand(any());
        verify(newsPort).updateNews(any());
        verify(newsMapper).toNewsUseCaseResult((NewsPortResult) any());
        verify(cache).put(any(), any());
    }
}



//    @Test
//    void shouldUpdateNews() {
//        //given
//        News news = TestData.generateNews();
//        UpdateNewsRequest newsRequest = TestData.generateUpdateNewsRequest();
//
//        when(newsRepository.findById(newsRequest.getId()))
//                .thenReturn(Optional.of(news));
//        when(newsMapper.toUpdateNewsRequest(news))
//                .thenReturn(newsRequest);
//
//        //when
//        UpdateNewsRequest actualNewsRequest = newsService.update(newsRequest);
//
//        //then
//        assertEquals(newsRequest, actualNewsRequest);
//        verify(newsRepository).findById(any());
//        verify(cache).put(any(), any());
//        verify(newsMapper).toUpdateNewsRequest(any());
//    }
//
//    @Test
//    void shouldNotUpdateNews_whenNewsNotFound() {
//        //given
//        UpdateNewsRequest newsRequest = TestData.generateUpdateNewsRequest();
//
//        when(newsRepository.findById(newsRequest.getId()))
//                .thenReturn(Optional.empty());
//
//        assertThrows(
//                NewsNotFoundException.class,
//                () -> newsService.update(newsRequest)
//        );
//        verify(newsRepository).findById(any());
//        verifyNoMoreInteractions(cache);
//    }