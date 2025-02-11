package ru.clevertec.service.news;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.cache.Cache;
import ru.clevertec.domain.News;
import ru.clevertec.mapper.DomainNewsMapper;
import ru.clevertec.port.input.news.command.NewsUseCaseResult;
import ru.clevertec.port.output.ReadNewsPort;
import ru.clevertec.port.output.port.NewsPortResult;
import ru.clevertec.util.news.TestData;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReadNewsServiceTest {

    @Mock
    ReadNewsPort newsPort;

    @Mock
    DomainNewsMapper newsMapper;

    @Mock
    Cache<UUID, News> cache;

    @InjectMocks
    ReadNewsService readNewsService;

    @Test
    void shouldReadAllNews() {
        //given
        int pageNumber = 0;
        int pageSize = 3;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        List<NewsPortResult> newsPortResults = TestData.generateListOfNewsPortResults();

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), newsPortResults.size());

        Page<NewsPortResult> newsPortResultPage =
                new PageImpl<>(newsPortResults.subList(start, end), pageRequest, newsPortResults.size());
        when(newsPort.readAllNews(pageRequest))
                .thenReturn(newsPortResultPage);

        List<NewsUseCaseResult> newsUseCaseResults = newsPortResultPage.stream()
                .map(newsMapper::toNewsUseCaseResult)
                .toList();

        //when
        Page<NewsUseCaseResult> actualNewsUseCaseResult = readNewsService.readAllNews(pageNumber, pageSize);

        //then
        assertEquals(newsUseCaseResults, actualNewsUseCaseResult.getContent());
        verify(newsPort).readAllNews(any());
        verify(newsMapper, times(newsUseCaseResults.size() * 2))
                .toNewsUseCaseResult(any(NewsPortResult.class));
    }

    @Test
    void shouldReadNewsById_whenNewsInCache() {
        //given
        UUID id = UUID.randomUUID();
        News news = TestData.generateNews();
        NewsUseCaseResult newsUseCaseResult = TestData.generateNewsUseCaseResult();

        when(cache.get(id))
                .thenReturn(Optional.of(news));
        when(newsMapper.toNewsUseCaseResult(news))
                .thenReturn(newsUseCaseResult);

        //when
        NewsUseCaseResult actualNewsUseCaseResult = readNewsService.readNewsById(id);

        //then
        assertEquals(newsUseCaseResult, actualNewsUseCaseResult);
        verify(cache).get(id);
        verify(newsMapper).toNewsUseCaseResult(any(News.class));
        verify(cache, never()).put(any(), any());
        verify(newsPort, never()).readNewsById(any());
    }

    @Test
    void shouldFindNewsById_whenNewsInDB() {
        //given
        UUID id = UUID.randomUUID();
        News news = TestData.generateNews();
        NewsUseCaseResult newsUseCaseResult = TestData.generateNewsUseCaseResult();
        NewsPortResult newsPortResult = TestData.generateNewsPortResult();

        when(cache.get(id))
                .thenReturn(Optional.empty());
        when(newsPort.readNewsById(id))
                .thenReturn(newsPortResult);
        when(newsMapper.toNews(newsPortResult))
                .thenReturn(news);
        when(newsMapper.toNewsUseCaseResult(news))
                .thenReturn(newsUseCaseResult);

        //when
        NewsUseCaseResult actualNewsUseCaseResult = readNewsService.readNewsById(id);

        //then
        assertEquals(newsUseCaseResult, actualNewsUseCaseResult);
        verify(newsPort).readNewsById(any());
        verify(newsMapper).toNews(any(NewsPortResult.class));
        verify(newsMapper).toNewsUseCaseResult(any(News.class));
        verify(cache).put(any(), any());
    }

}


//    @Test
//    void shouldFindNewsById_whenNewsInDB() {
//        //given
//        News news = TestData.generateNews();
//        NewsResponse newsResponse = TestData.generateNewsResponse();
//
//        when(cache.get(newsResponse.getId()))
//                .thenReturn(Optional.empty());
//        when(newsRepository.findById(newsResponse.getId()))
//                .thenReturn(Optional.of(news));
//        when(newsMapper.toNewsResponse(news))
//                .thenReturn(newsResponse);
//
//        //when
//        NewsResponse actualNewsResponse = newsService.findById(newsResponse.getId());
//
//        //then
//        assertEquals(newsResponse, actualNewsResponse);
//        verify(newsRepository).findById(any());
//        verify(cache).put(any(), any());
//    }
//
//    @Test
//    void shouldNotFindNewsById_whenNewsNotFound() {
//        //given
//        NewsResponse newsResponse = TestData.generateNewsResponse();
//
//        when(cache.get(newsResponse.getId()))
//                .thenReturn(Optional.empty());
////        when(newsRepository.findById(newsResponse.getId()))
////                .thenReturn(Optional.empty());
//        doThrow(NewsNotFoundException.class)
//                .when(newsRepository).findById(newsResponse.getId());
//
//        //when, then
//        assertThrows(
//                NewsNotFoundException.class,
//                () -> newsService.findById(newsResponse.getId())
//        );
//        verify(newsRepository).findById(any());
//        verifyNoMoreInteractions(cache);
//    }
//
