package ru.clevertec.util;

import ru.clevertec.adapter.input.web.news.dto.NewsRequest;
import ru.clevertec.adapter.input.web.news.dto.NewsResponse;
import ru.clevertec.adapter.input.web.news.dto.NewsUpdateRequest;
import ru.clevertec.adapter.output.persistence.jpa.entity.NewsEntity;
import ru.clevertec.port.input.news.command.NewsCreateCommand;
import ru.clevertec.port.input.news.command.NewsUpdateCommand;
import ru.clevertec.port.input.news.command.NewsUseCaseResult;
import ru.clevertec.port.output.port.NewsCreatePortCommand;
import ru.clevertec.port.output.port.NewsPortResult;
import ru.clevertec.port.output.port.NewsUpdatePortCommand;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestData {
    private static final UUID NEWS_ID = UUID.randomUUID();
    private static final UUID AUTHOR_ID = UUID.randomUUID();
    private static final LocalDateTime FIXED_TIME = LocalDateTime.parse("2024-12-18T15:30:00");

    public static NewsRequest generateNewsRequest() {
        return NewsRequest.builder()
                .title("Title")
                .text("Text")
                .time(FIXED_TIME)
                .build();
    }

    public static NewsResponse generateNewsResponse() {
        return NewsResponse.builder()
                .id(NEWS_ID)
                .title("Title")
                .text("Text")
                .time(FIXED_TIME)
                .authorId(AUTHOR_ID)
                .build();
    }

    public static NewsUseCaseResult generateNewsUseCaseResult() {
        return NewsUseCaseResult.builder()
                .id(NEWS_ID)
                .title("Title")
                .text("Text")
                .time(FIXED_TIME)
                .authorId(AUTHOR_ID)
                .build();
    }

    public static NewsCreateCommand generateNewsCreateCommand() {
        return NewsCreateCommand.builder()
                .title("Title")
                .text("Text")
                .time(FIXED_TIME)
                .authorId(AUTHOR_ID)
                .build();
    }

    public static NewsCreatePortCommand generateNewsCreatePortCommand() {
        return NewsCreatePortCommand.builder()
                .title("Title")
                .text("Text")
                .time(FIXED_TIME)
                .authorId(AUTHOR_ID)
                .build();
    }

    public static NewsUpdatePortCommand generateNewsUpdatePortCommand() {
        return NewsUpdatePortCommand.builder()
                .id(NEWS_ID)
                .title("Title")
                .text("Text")
                .time(FIXED_TIME)
                .authorId(AUTHOR_ID)
                .build();
    }

    public static NewsPortResult generateNewsPortResult() {
        return NewsPortResult.builder()
                .id(NEWS_ID)
                .title("Title")
                .text("Text")
                .time(FIXED_TIME)
                .authorId(AUTHOR_ID)
                .build();
    }

    public static NewsUpdateCommand generateNewsUpdateCommand() {
        return NewsUpdateCommand.builder()
                .id(NEWS_ID)
                .title("Title")
                .text("Text")
                .time(FIXED_TIME)
                .authorId(AUTHOR_ID)
                .build();
    }

    public static NewsUpdateRequest generateNewsUpdateRequest() {
        return NewsUpdateRequest.builder()
                .id(NEWS_ID)
                .title("Title")
                .text("Text")
                .time(FIXED_TIME)
                .build();
    }

    public static NewsEntity generateNewsEntity() {
        return NewsEntity.builder()
                .id(NEWS_ID)
                .title("Title")
                .text("Text")
                .time(FIXED_TIME)
                .authorId(AUTHOR_ID)
                .build();
    }

    public static List<NewsUseCaseResult> generateListOfNewsUseCaseResults() {
        int count = (int) (Math.random() * 100) + 3;
        ArrayList<NewsUseCaseResult> newsUseCaseResults = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            newsUseCaseResults.add(generateNewsUseCaseResult());
        }
        return newsUseCaseResults;
    }

    public static List<NewsEntity> generateListOfNewsEntities() {
        int count = (int) (Math.random() * 100) + 3;
        ArrayList<NewsEntity> newsEntities = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            newsEntities.add(generateNewsEntity());
        }
        return newsEntities;
    }
}
