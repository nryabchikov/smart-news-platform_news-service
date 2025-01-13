package ru.clevertec.cache;

import java.util.Optional;

/**
 * Интерфейс для кэша.  Определяет основные операции: получение, добавление и удаление элементов из кеша.
 *
 * @param <K> Тип ключа.
 * @param <V> Тип значения.
 */
public interface Cache<K, V> {

    /**
     * Возвращает значение по ключу.
     *
     * @param key Ключ.
     * @return Значение, в обертке Optional или Optional.empty(), если ключ не найден.
     */
    Optional<V> get(K key);

    /**
     * Добавляет пару ключ-значение в кэш.
     *
     * @param key   Ключ.
     * @param value Значение.
     */
    void put(K key, V value);

    /**
     * Удаляет элемент из кэша по ключу.
     *
     * @param key Ключ.
     */
    void delete(K key);
}

