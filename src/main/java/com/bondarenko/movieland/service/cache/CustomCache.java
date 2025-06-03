package com.bondarenko.movieland.service.cache;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

@Slf4j
public class CustomCache<T> {
    private final CopyOnWriteArrayList<T> cache = new CopyOnWriteArrayList<>();
    private final Supplier<List<T>> dbFetcher;

    public CustomCache(Supplier<List<T>> dbFetcher) {
        this.dbFetcher = dbFetcher;
    }

    public Set<T> getAll() {
        if (cache.isEmpty()) {
            throw new IllegalStateException("Cache is empty");
        }
        log.info("Cache accessed, size: {}", cache.size());
        return new HashSet<>(cache);
    }

    public void refresh() {
        List<T> dataFromDb = dbFetcher.get();
        if (cache.equals(dataFromDb)) {
            log.info("Кеш залишиться незмінним");
            return;
        }
        cache.clear();
        cache.addAll(dataFromDb);
        log.info("Cache updated, new size: {}", cache.size());
    }

}
