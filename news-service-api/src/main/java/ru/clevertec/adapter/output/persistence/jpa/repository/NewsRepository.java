package ru.clevertec.adapter.output.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.adapter.output.persistence.jpa.entity.NewsEntity;

import java.util.UUID;

/**
 * Репозиторий JPA для работы с сущностью NewsEntity.
 */
public interface NewsRepository extends JpaRepository<NewsEntity, UUID> {
}
