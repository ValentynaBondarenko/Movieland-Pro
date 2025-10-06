package com.bondarenko.movieland.service.cache;

import lombok.extern.slf4j.Slf4j;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

@Slf4j
public class SoftReferenceCache<K, V> {
    private final Function<K, V> valueLoader;
    private final Map<K, SoftReference<V>> cache = new ConcurrentHashMap<>();
    private final Map<K, CompletableFuture<V>> inProgress = new ConcurrentHashMap<>();

    public SoftReferenceCache(Function<K, V> valueLoader) {
        this.valueLoader = valueLoader;
    }

    /**
     * Повертає значення з кешу або завантажує через valueLoader.
     * Якщо кілька потоків одночасно викликають get для одного ключа —
     * тільки один виконує завантаження, інші чекають.
     */
    V get(K key) {
        V cachedValue = getFromCache(key);
        if (cachedValue != null) {
            return cachedValue;
        }

        CompletableFuture<V> loadingFuture = getOrLoadFuture(key);

        return waitForFuture(loadingFuture, key);
    }
    public void put(K key, V value) {
        if (value != null) cache.put(key, new SoftReference<>(value));

        CompletableFuture<V> future = inProgress.remove(key);
        if (future != null && !future.isDone()) {
            future.complete(value);
        }
    }
    public void update(K key, V value) {
        if (value != null) {
            cache.put(key, new SoftReference<>(value));
            log.debug("Cache UPDATED for key {}", key);
        } else {
            cache.remove(key);
            log.debug("Cache REMOVED for key {} because value is null", key);
        }

        CompletableFuture<V> inProgressFuture = inProgress.get(key);
        if (inProgressFuture != null && !inProgressFuture.isDone()) {
            inProgressFuture.complete(value);
            inProgress.remove(key);
        }
    }


    //50 запитів в базу іде. а треба 1 всі ждуть
    // FullMovieResponse movie = movieService.getMovieById(movieId, null);
    //    movieCache.put(movieId, new SoftReference<>(movie));

    /**
     * Видаляє значення з кешу.
     *
     * @param key ключ для видалення
     * @return значення, яке було в кеші, або null
     */
    public V delete(K key) {
        SoftReference<V> removedReference = cache.remove(key);

        V removedValue = (removedReference != null) ? removedReference.get() : null;

        log.debug("Cache DELETED for key {}", key);

        // Якщо є активний CompletableFuture, завершуємо його null
        CompletableFuture<V> inProgressFuture = inProgress.remove(key);
        if (inProgressFuture != null && !inProgressFuture.isDone()) {
            inProgressFuture.complete(null);
        }

        return removedValue;
    }

    private V getFromCache(K key) {
        SoftReference<V> valueReference = cache.get(key);
        V value = (valueReference != null) ? valueReference.get() : null;

        if (value != null) {
            log.debug("Cache HIT for key {}", key);
        } else {
            log.debug("Cache MISS for key {}", key);
        }

        return value;
    }

    private CompletableFuture<V> getOrLoadFuture(K key) {
        return inProgress.computeIfAbsent(key, cacheKey -> {
            CompletableFuture<V> futureValue = new CompletableFuture<>();

            CompletableFuture.runAsync(() -> loadValueAsync(cacheKey, futureValue));

            return futureValue;
        });
    }

    private void loadValueAsync(K cacheKey, CompletableFuture<V> futureValue) {
        try {
            V loadedValue = valueLoader.apply(cacheKey);
            if (loadedValue != null) {
                cache.put(cacheKey, new SoftReference<>(loadedValue));
            }
            futureValue.complete(loadedValue);
        } catch (Exception e) {
            futureValue.completeExceptionally(e);
        } finally {
            futureValue.whenComplete((r, ex) -> inProgress.remove(cacheKey));
        }
    }

    private V waitForFuture(CompletableFuture<V> future, K key) {
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Cache loading interrupted for key: " + key, e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Cache loading failed for key: " + key, e.getCause());
        }
    }

}
