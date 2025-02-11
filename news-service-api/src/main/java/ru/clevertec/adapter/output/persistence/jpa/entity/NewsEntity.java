package ru.clevertec.adapter.output.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность, представляющая новость в базе данных.
 */
@Entity
@Table(name = "news")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class NewsEntity {

    /** Уникальный ID новости. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private UUID id;

    /** Заголовок новости. */
    @Column(name = "title")
    private String title;

    /** Текст новости. */
    @Column(name = "text")
    private String text;

    /** Дата и время публикации новости. */
    @Column(name = "time")
    private LocalDateTime time;

    /** ID автора новости. */
    @Column(name = "author_id")
    private UUID authorId;
}
