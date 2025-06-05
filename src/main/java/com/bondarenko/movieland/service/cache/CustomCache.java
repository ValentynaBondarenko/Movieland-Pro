package com.bondarenko.movieland.service.cache;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Supplier;

@Slf4j
public class CustomCache<T> {
    private final Set<T> cache = new CopyOnWriteArraySet<>();
    private final Supplier<List<T>> dbFetcher;

    public CustomCache(Supplier<List<T>> dbFetcher) {
        this.dbFetcher = dbFetcher;
    }

    public Set<T> getAll() {
        return new HashSet<>(cache);
    }

    public void refresh() {
        List<T> dataFromDb = dbFetcher.get();
        cache.addAll(dataFromDb);
        log.info("Cache updated, new size: {}", cache.size());
    }

}
