package com.bondarenko.movieland.service.cache;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class SoftReferenceCache<K, V> {
    private final Function<K, V> valueLoader;
    private final Map<K, SoftReference<V>> cache = new ConcurrentHashMap<>();

    public SoftReferenceCache(Function<K, V> valueLoader) {
        this.valueLoader = valueLoader;
    }


    public V get(K key) {
        // Атомарне обчислення нового SoftReference для ключа
        SoftReference<V> softRef = cache.compute(key, (k, oldRef) -> {
            V value = (oldRef != null) ? oldRef.get() : null;
            if (value == null) {
                value = valueLoader.apply(k); // створюємо нове значення
                return new SoftReference<>(value); // без referenceQueue
            } else {
                return oldRef; // залишаємо існуючий SoftReference
            }
        });

        return softRef.get(); // повертаємо значення
    }


    public int liveReferencesCount() {
        int count = 0;
        for (SoftReference<V> softReference : cache.values()) {
            if (softReference.get() != null) {
                count++;
            }
        }
        return count;
    }

}