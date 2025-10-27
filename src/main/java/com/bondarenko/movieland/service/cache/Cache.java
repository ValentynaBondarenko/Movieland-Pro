package com.bondarenko.movieland.service.cache;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.StampedLock;
import java.util.function.Supplier;

@Slf4j
public class Cache<T> {
    private volatile List<T> dataCache = new ArrayList<>();
    private final Supplier<List<T>> dataFetcher;

    public Cache(Supplier<List<T>> dataFetcher) {
        this.dataFetcher = dataFetcher;
    }

    protected List<T> getAll() {
        return new ArrayList<>(dataCache);
    }

    protected void refresh() {
        List<T> data = dataFetcher.get();
        if (data == null) {
            data = Collections.emptyList();
            log.warn("Cache returned empty list");
        }
        dataCache = List.copyOf(data);
        log.info("Cache updated, new size: {}", dataCache.size());
    }
}
