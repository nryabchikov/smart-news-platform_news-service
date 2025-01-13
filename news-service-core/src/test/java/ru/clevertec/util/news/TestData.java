package ru.clevertec.util.news;


import ru.clevertec.domain.News;
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
    public static NewsCreateCommand generateNewsCreateCommand() {
        return NewsCreateCommand.builder()
                .title("Title")
                .text("Some text...")
                .time(LocalDateTime.now())
                .authorId(UUID.randomUUID())
                .build();
    }

    public static NewsCreatePortCommand generateNewsCreatePortCommand() {
        return NewsCreatePortCommand.builder()
                .title("Title")
                .text("Some text...")
                .time(LocalDateTime.now())
                .authorId(UUID.randomUUID())
                .build();
    }

    public static NewsPortResult generateNewsPortResult() {
        return NewsPortResult.builder()
                .title("Title")
                .text("Some text...")
                .time(LocalDateTime.now())
                .authorId(UUID.randomUUID())
                .build();
    }

    public static NewsUseCaseResult generateNewsUseCaseResult() {
        return NewsUseCaseResult.builder()
                .title("Title")
                .text("Some text...")
                .time(LocalDateTime.now())
                .authorId(UUID.randomUUID())
                .build();
    }

    public static NewsUpdateCommand generateNewsUpdateCommand() {
        return NewsUpdateCommand.builder()
                .id(UUID.randomUUID())
                .title("Title")
                .text("Some text...")
                .time(LocalDateTime.now())
                .authorId(UUID.randomUUID())
                .build();
    }

    public static NewsUpdatePortCommand generateNewsUpdatePortCommand() {
        return NewsUpdatePortCommand.builder()
                .id(UUID.randomUUID())
                .title("Title")
                .text("Some text...")
                .time(LocalDateTime.now())
                .authorId(UUID.randomUUID())
                .build();
    }

    public static News generateNews() {
        return News.builder()
                .id(UUID.randomUUID())
                .title("Title")
                .text("Some text...")
                .time(LocalDateTime.now())
                .authorId(UUID.randomUUID())
                .build();
    }

    public static List<NewsPortResult> generateListOfNewsPortResults() {
        int count = (int) (Math.random() * 100);
        ArrayList<NewsPortResult> newsPortResults = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            newsPortResults.add(generateNewsPortResult());
        }
        return newsPortResults;
    }
}
