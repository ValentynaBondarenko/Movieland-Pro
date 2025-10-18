package com.bondarenko.movieland.service.cache;

import lombok.extern.slf4j.Slf4j;

import java.lang.ref.ReferenceQueue;
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
    private final ReferenceQueue<V> referenceQueue = new ReferenceQueue<>();

    public SoftReferenceCache(Function<K, V> valueLoader) {
        this.valueLoader = valueLoader;
    }

    public V get(K key) {
        V cachedValue = getFromCache(key);
        if (cachedValue != null) {
            return cachedValue;
        }

        CompletableFuture<V> loadingFuture = getOrLoadFuture(key);
        return waitForFuture(loadingFuture, key);
    }

    private V getFromCache(K key) {
        SoftReference<V> ref = cache.get(key);
        V value = (ref != null) ? ref.get() : null;
        if (value != null) {
            log.debug("Cache HIT for key {}", key);
        } else {
            log.debug("Cache MISS for key {}", key);
        }
        return value;
    }

    private CompletableFuture<V> getOrLoadFuture(K key) {
        return inProgress.computeIfAbsent(key, k -> {
            CompletableFuture<V> future = new CompletableFuture<>();
            CompletableFuture.runAsync(() -> loadValueAsync(k, future));
            return future;
        });
    }

    private void loadValueAsync(K key, CompletableFuture<V> future) {
        try {
            V loaded = valueLoader.apply(key);
            if (loaded != null) {
                cache.put(key, new SoftReference<>(loaded, referenceQueue));
            }
            future.complete(loaded);
        } catch (Exception e) {
            future.completeExceptionally(e);
        } finally {
            inProgress.remove(key);
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

    public void clear() {
        cache.clear();
    }
}
