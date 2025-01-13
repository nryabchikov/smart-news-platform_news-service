package ru.clevertec.adapter.output.persistence.adapter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.adapter.output.persistence.PersistenceNewsMapper;
import ru.clevertec.adapter.output.persistence.jpa.entity.NewsEntity;
import ru.clevertec.adapter.output.persistence.jpa.repository.NewsRepository;
import ru.clevertec.exception.NewsNotFoundException;
import ru.clevertec.port.output.port.NewsCreatePortCommand;
import ru.clevertec.port.output.port.NewsPortResult;
import ru.clevertec.port.output.port.NewsUpdatePortCommand;
import ru.clevertec.util.TestData;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WriteNewsAdapterTest {

    @Mock
    NewsRepository newsRepository;

    @Mock
    PersistenceNewsMapper newsMapper;

    @InjectMocks
    WriteNewsAdapter writeNewsAdapter;

    @Test
    void shouldCreateNews() {
        //given
        NewsEntity newsEntity = TestData.generateNewsEntity();
        NewsPortResult newsPortResult = TestData.generateNewsPortResult();
        NewsCreatePortCommand newsCreatePortCommand = TestData.generateNewsCreatePortCommand();

        when(newsMapper.toNewsEntity(newsCreatePortCommand))
                .thenReturn(newsEntity);
        when(newsRepository.save(newsEntity))
                .thenReturn(newsEntity);
        when(newsMapper.toNewsPortResult(newsEntity))
                .thenReturn(newsPortResult);

        //when
        NewsPortResult actualNewsPortResult = writeNewsAdapter.createNews(newsCreatePortCommand);

        //then
        assertEquals(newsPortResult, actualNewsPortResult);
        verify(newsMapper).toNewsEntity(any());
        verify(newsRepository).save(any());
        verify(newsMapper).toNewsPortResult(any());
    }

    @Test
    void shouldUpdateNews_whenNewsExist() {
        //given
        NewsEntity newsEntity = TestData.generateNewsEntity();
        NewsPortResult newsPortResult = TestData.generateNewsPortResult();
        NewsUpdatePortCommand newsUpdatePortCommand = TestData.generateNewsUpdatePortCommand();

        when(newsRepository.findById(newsUpdatePortCommand.id()))
                .thenReturn(Optional.of(newsEntity));
        when(newsMapper.toNewsPortResult(newsEntity))
                .thenReturn(newsPortResult);

        //when
        NewsPortResult actualNewsPortResult = writeNewsAdapter.updateNews(newsUpdatePortCommand);

        //then
        assertEquals(newsPortResult, actualNewsPortResult);
        verify(newsRepository).findById(any());
        verify(newsMapper).toNewsPortResult(any());
    }

    @Test
    void shouldNotUpdateNews_whenNewsNotExist() {
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

    @Test
    void shouldDeleteNewsById() {
        //given
        UUID newsId = UUID.randomUUID();

        //when
        writeNewsAdapter.deleteNewsById(newsId);

        //then
        verify(newsRepository).deleteById(newsId);
        verifyNoMoreInteractions(newsRepository);
    }
}