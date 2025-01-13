package ru.clevertec.cache;

import lombok.extern.slf4j.Slf4j;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Реализация кэша с алгоритмом Least Recently Used (LRU).
 *
 * @param <K> Тип ключа.
 * @param <V> Тип значения.
 */
@Slf4j
public class LRUCache<K, V> implements Cache<K, V> {

    /**
     * Максимальный размер кэша.
     */
    private final int maxSize;

    /**
     * Кэш, использующий LinkedHashMap для автоматического удаления наименее недавно используемых элементов.
     */
    private final Map<K, V> cache;

    /**
     * Создает новый экземпляр LRUCache с заданным максимальным размером.
     *
     * @param maxSize Максимальный размер кэша.
     */
    public LRUCache(int maxSize) {
        this.maxSize = maxSize;
        this.cache = new LinkedHashMap<>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                boolean removed = size() > maxSize;
                if (removed) {
                    log.debug("LRUCache: Evicting eldest entry: {}", eldest);
                }
                return removed;
            }
        };
        log.info("LRUCache created with maxSize: {}", maxSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<V> get(K key) {
        log.info("LRUCache.get: Getting value for key: {}", key);
        V value = cache.get(key);
        if (value != null) {
            log.debug("LRUCache.get: Value found for key: {}", key);
            // LinkedHashMap automatically moves accessed entry to the end
            return Optional.of(value);
        } else {
            log.debug("LRUCache.get: Value not found for key: {}", key);
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(K key, V value) {
        log.info("LRUCache.put: Putting value for key: {}, value: {}", key, value);
        cache.put(key, value);
        log.debug("LRUCache.put: Value added for key: {}", key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(K key) {
        log.info("LRUCache.delete: Deleting value for key: {}", key);
        cache.remove(key);
        log.debug("LRUCache.delete: Value deleted for key: {}", key);
    }
}
