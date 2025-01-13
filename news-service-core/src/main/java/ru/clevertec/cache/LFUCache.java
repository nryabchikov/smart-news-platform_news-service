package ru.clevertec.cache;

import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Реализация кэша с алгоритмом Least Frequently Used (LFU).
 *
 * @param <K> Тип ключа.
 * @param <V> Тип значения.
 */
@Slf4j
public class LFUCache<K, V> implements Cache<K, V> {

    /**
     * Максимальный размер кэша.
     */
    private final int maxSize;

    /**
     * Кэш, хранящий пары ключ-значение. Используется LinkedHashMap для сохранения порядка добавления.
     */
    private final Map<K, Node<K, V>> cache = new LinkedHashMap<>();

    /**
     * Хранит частоту использования каждого элемента.
     */
    private final Map<K, Integer> frequency = new HashMap<>();

    /**
     * Хранит элементы в порядке возрастания частоты использования.
     * Используется LinkedHashSet для сохранения порядка добавления.
     */
    private final Set<Node<K, V>> frequencyOrder = new LinkedHashSet<>();

    /**
     * Создает новый экземпляр LFUCache с заданным максимальным размером.
     *
     * @param maxSize Максимальный размер кэша.
     */
    public LFUCache(int maxSize) {
        this.maxSize = maxSize;
        log.info("LFUCache created with maxSize: {}", maxSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<V> get(K key) {
        log.info("LFUCache.get: Getting value for key: {}", key);
        Node<K, V> node = cache.get(key);
        if (node != null) {
            updateFrequency(node);
            log.debug("LFUCache.get: Value found for key: {}", key);
            return Optional.of(node.value);
        }
        log.debug("LFUCache.get: Value not found for key: {}", key);
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(K key, V value) {
        log.info("LFUCache.put: Putting value for key: {}, value: {}", key, value);
        if (cache.size() >= maxSize) {
            evictLeastFrequentlyUsed();
        }

        Node<K, V> node = new Node<>(key, value);
        cache.put(key, node);
        frequency.put(key, 1);
        frequencyOrder.add(node);
        log.debug("LFUCache.put: Value added for key: {}", key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(K key) {
        log.info("LFUCache.delete: Deleting value for key: {}", key);
        cache.remove(key);
        frequency.remove(key);
        frequencyOrder.removeIf(node -> node.key.equals(key));
        log.debug("LFUCache.delete: Value deleted for key: {}", key);
    }

    /**
     * Удаляет наименее часто используемый элемент из кэша.
     */
    private void evictLeastFrequentlyUsed() {
        log.debug("LFUCache.evictLeastFrequentlyUsed: Evicting least frequently used item.");
        int minFrequency = Integer.MAX_VALUE;
        Node<K, V> nodeToEvict = null;
        for (Node<K, V> node : frequencyOrder) {
            if (frequency.get(node.key) < minFrequency) {
                minFrequency = frequency.get(node.key);
                nodeToEvict = node;
            }
        }

        if (nodeToEvict != null) {
            cache.remove(nodeToEvict.key);
            frequency.remove(nodeToEvict.key);
            frequencyOrder.remove(nodeToEvict);
            log.debug("LFUCache.evictLeastFrequentlyUsed: Item evicted: {}", nodeToEvict.key);
        } else {
            log.warn("LFUCache.evictLeastFrequentlyUsed: No item to evict.");
        }
    }

    /**
     * Обновляет частоту использования элемента.
     *
     * @param node Узел, частоту которого нужно обновить.
     */
    private void updateFrequency(Node<K, V> node) {
        log.debug("LFUCache.updateFrequency: Updating frequency for key: {}", node.key);
        int currentFrequency = frequency.get(node.key);
        frequency.put(node.key, currentFrequency + 1);

        frequencyOrder.remove(node);
        frequencyOrder.add(node);
        log.debug("LFUCache.updateFrequency: Frequency updated for key: {}", node.key);
    }

    /**
     * Внутренний класс, представляющий узел в кэше.
     *
     * @param <K> Тип ключа.
     * @param <V> Тип значения.
     */
    private static class Node<K, V> {
        K key;
        V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?, ?> node = (Node<?, ?>) o;
            return Objects.equals(key, node.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }
}
