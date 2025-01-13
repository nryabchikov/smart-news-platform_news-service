package ru.clevertec.util.comment;

import ru.clevertec.client.comment.dto.CommentRequest;
import ru.clevertec.client.comment.dto.CommentResponse;
import ru.clevertec.client.comment.dto.CommentUpdateRequest;
import ru.clevertec.port.input.comment.command.CommentCreateCommand;
import ru.clevertec.port.input.comment.command.CommentUpdateCommand;
import ru.clevertec.port.input.comment.command.CommentUseCaseResult;
import ru.clevertec.port.output.port.NewsPortResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestData {

    public static CommentCreateCommand generateCommentCreateCommand() {
        return CommentCreateCommand.builder()
                .text("Some text!")
                .time(LocalDateTime.now())
                .newsId(UUID.randomUUID())
                .authorId(UUID.randomUUID())
                .build();
    }

    public static CommentUpdateCommand generateCommentUpdateCommand() {
        return CommentUpdateCommand.builder()
                .id(UUID.randomUUID())
                .text("Some text!")
                .time(LocalDateTime.now())
                .newsId(UUID.randomUUID())
                .authorId(UUID.randomUUID())
                .build();
    }

    public static CommentUseCaseResult generateCommentUseCaseResult() {
        return CommentUseCaseResult.builder()
                .id(UUID.randomUUID())
                .text("Some text!")
                .time(LocalDateTime.now())
                .newsId(UUID.randomUUID())
                .authorId(UUID.randomUUID())
                .build();
    }

    public static CommentRequest generateCommentRequest() {
        return CommentRequest.builder()
                .text("Some text!")
                .time(LocalDateTime.now())
                .build();
    }

    public static CommentUpdateRequest generateCommentUpdateRequest() {
        return CommentUpdateRequest.builder()
                .id(UUID.randomUUID())
                .text("Some text!")
                .time(LocalDateTime.now())
                .build();
    }

    public static CommentResponse generateCommentResponse() {
        return CommentResponse.builder()
                .id(UUID.randomUUID())
                .text("Some text!")
                .time(LocalDateTime.now())
                .newsId(UUID.randomUUID())
                .authorId(UUID.randomUUID())
                .build();
    }

    public static List<CommentResponse> generateListOfCommentResponses() {
        int count = (int) (Math.random() * 100);
        ArrayList<CommentResponse> commentResponses = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            commentResponses.add(generateCommentResponse());
        }
        return commentResponses;
    }

    public static List<CommentUseCaseResult> generateListOfCommentUseCaseResults() {
        int count = (int) (Math.random() * 100);
        ArrayList<CommentUseCaseResult> commentUseCaseResults = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            commentUseCaseResults.add(generateCommentUseCaseResult());
        }
        return commentUseCaseResults;
    }
}
