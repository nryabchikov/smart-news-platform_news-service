package ru.clevertec.adapter.output.persistence.adapter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.adapter.output.persistence.PersistenceNewsMapper;
import ru.clevertec.adapter.output.persistence.jpa.entity.NewsEntity;
import ru.clevertec.adapter.output.persistence.jpa.repository.NewsRepository;
import ru.clevertec.exception.NewsNotFoundException;
import ru.clevertec.port.output.port.NewsPortResult;
import ru.clevertec.util.TestData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class ReadNewsAdapterTest {

    @Mock
    NewsRepository newsRepository;

    @Mock
    PersistenceNewsMapper newsMapper;

    @InjectMocks
    ReadNewsAdapter readNewsAdapter;

    @Test
    void shouldReadAllNews() {
        //given
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<NewsEntity> newsEntities = TestData.generateListOfNewsEntities();
        List<NewsPortResult> newsPortResults = new ArrayList<>();

        when(newsRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(newsEntities, pageable, newsEntities.size()));
        for (NewsEntity newsEntity : newsEntities) {
            NewsPortResult mappedResult = TestData.generateNewsPortResult();
            when(newsMapper.toNewsPortResult(newsEntity))
                    .thenReturn(mappedResult);
            newsPortResults.add(mappedResult);
        }

        //when
        Page<NewsPortResult> actualNewsPortResult =
                readNewsAdapter.readAllNews(pageable);

        //then
        assertEquals(newsPortResults, actualNewsPortResult.getContent());
        verify(newsRepository).findAll(any(Pageable.class));
        verify(newsMapper, times(newsEntities.size())).toNewsPortResult(any());
    }

    @Test
    void shouldReadNewsById_whenNewsExist() {
        //given
        UUID newsId = UUID.randomUUID();
        NewsEntity newsEntity = TestData.generateNewsEntity();
        NewsPortResult newsPortResult = TestData.generateNewsPortResult();

        when(newsRepository.findById(newsId))
                .thenReturn(Optional.of(newsEntity));
        when(newsMapper.toNewsPortResult(newsEntity))
                .thenReturn(newsPortResult);

        //when
        NewsPortResult actualNewsPortResult = readNewsAdapter.readNewsById(newsId);

        //then
        assertEquals(newsPortResult, actualNewsPortResult);
        verify(newsRepository).findById(any());
        verify(newsMapper).toNewsPortResult(any());
    }

    @Test
    void shouldNotReadNewsById_whenNewsNotExist() {
        //given
        UUID newsId = UUID.randomUUID();

        doThrow(NewsNotFoundException.class)
                .when(newsRepository).findById(newsId);

        //when, then
        assertThrows(
                NewsNotFoundException.class,
                () -> newsRepository.findById(newsId)
        );
        verify(newsRepository).findById(any());
        verify(newsMapper, never()).toNewsPortResult(any());
    }
}