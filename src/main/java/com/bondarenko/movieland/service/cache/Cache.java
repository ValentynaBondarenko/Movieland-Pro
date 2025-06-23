package com.bondarenko.movieland.service.cache;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

@Slf4j
public class Cache<T> {
    private final List<T> cache = new CopyOnWriteArrayList<>();
    private final Supplier<List<T>> dbFetcher;

    public Cache(Supplier<List<T>> dbFetcher) {
        this.dbFetcher = dbFetcher;
    }

    public List<T> getAll() {
        return new ArrayList<>(cache);
    }

    public void refresh() {
        List<T> dataFromDb = dbFetcher.get();
        addIfNotPresent(dataFromDb);
        log.info("Cache updated, new size: {}", cache.size());
    }

    private void addIfNotPresent(List<T> data) {
        data.stream()
                .filter(element -> !cache.contains(element))
                .forEach(cache::add);
    }

}
