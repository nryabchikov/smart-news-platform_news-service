package ru.clevertec.util;

import ru.clevertec.adapter.input.web.news.dto.NewsRequest;
import ru.clevertec.adapter.input.web.news.dto.NewsResponse;
import ru.clevertec.adapter.input.web.news.dto.NewsUpdateRequest;

import java.time.LocalDateTime;
import java.util.UUID;

public class TestDataIT {

    public static NewsResponse generateNewsResponse() {
        return NewsResponse.builder()
                .id(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .title("Breaking News 1")
                .text("Text for news 1")
                .time(LocalDateTime.parse("2025-01-01T10:15:00"))
                .authorId(UUID.fromString("a1e13d4e-0000-4000-8000-000000000001"))
                .build();
    }

    public static NewsRequest generateNewsRequest() {
        return NewsRequest.builder()
                .title("Breaking News 1")
                .text("Text for news 1")
                .time(LocalDateTime.parse("2025-01-01T10:15:00"))
                .build();
    }

    public static NewsUpdateRequest generateNewsUpdateRequest() {
        return NewsUpdateRequest.builder()
                .id(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .title("Breaking News 1")
                .text("Text for news 1")
                .time(LocalDateTime.parse("2025-01-01T10:15:00"))
                .build();
    }
}
