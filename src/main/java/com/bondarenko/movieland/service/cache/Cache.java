package com.bondarenko.movieland.service.cache;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.StampedLock;
import java.util.function.Supplier;

@Slf4j
public class Cache<T> {
    private final List<T> cache = new ArrayList<>();
    private final Supplier<List<T>> dataFetcher;
    private StampedLock lock = new StampedLock();

    public Cache(Supplier<List<T>> dataFetcher) {
        this.dataFetcher = dataFetcher;
    }

    public List<T> getAll() {
        long stamp = lock.tryOptimisticRead();
        List<T> snapshot = new ArrayList<>(cache);
        if (!lock.validate(stamp)) {
            stamp = lock.readLock();
            try {
                snapshot = new ArrayList<>(cache);
            } finally {
                lock.unlockRead(stamp);
                log.debug("Read lock released with stamp: {}", stamp);
            }
        }
        return snapshot;
    }

    public void refresh() {
        List<T> data = dataFetcher.get();
        long stamp = lock.writeLock();
        try {
            addIfNotPresent(data);
            log.info("Cache updated, new size: {}", cache.size());
        } finally {
            lock.unlockWrite(stamp);
            log.debug("Write lock released with stamp: {}", stamp);
        }
    }

    public void addIfNotPresent(List<T> data) {
        data.stream()
                .filter(element -> !cache.contains(element))
                .forEach(cache::add);
    }

}
