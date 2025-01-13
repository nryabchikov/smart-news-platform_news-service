package ru.clevertec.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.cache.Cache;
import ru.clevertec.cache.LFUCache;
import ru.clevertec.cache.LRUCache;
import ru.clevertec.domain.News;

import java.util.UUID;

/**
 * Конфигурационный класс для настройки кэша. Выбирает тип кэша (LRU или LFU) на основе свойства `cache.type`.
 */
@Configuration
public class CacheConfig {

    /**
     * Максимальный размер кэша.  Значение по умолчанию: 10.
     */
    @Value("${cache.max-size:10}")
    private int maxSize;

    /**
     * Создает и возвращает кэш LRU, если свойство `cache.type` равно "lru" или не задано.
     *
     * @return Кэш LRU {@link LRUCache}.
     */
    @Bean
    @ConditionalOnProperty(value = "cache.type", havingValue = "lru", matchIfMissing = true)
    public Cache<UUID, News> lruCache() {
        return new LRUCache<>(maxSize);
    }

    /**
     * Создает и возвращает кэш LFU, если свойство `cache.type` равно "lfu".
     *
     * @return Кэш LFU {@link LFUCache}.
     */
    @Bean
    @ConditionalOnProperty(value = "cache.type", havingValue = "lfu")
    public Cache<UUID, News> lfuCache() {
        return new LFUCache<>(maxSize);
    }
}
