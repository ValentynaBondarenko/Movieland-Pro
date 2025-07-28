package com.bondarenko.movieland.service.cache;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.StampedLock;
import java.util.function.Supplier;

@Slf4j
public class Cache<T> {
    private final List<T> dataCache = new ArrayList<>();
    private final Supplier<List<T>> dataFetcher;
    private final StampedLock lock = new StampedLock();

    public Cache(Supplier<List<T>> dataFetcher) {
        this.dataFetcher = dataFetcher;
    }

    protected List<T> getAll() {
        long stamp = lock.tryOptimisticRead();

        List<T> snapshot = new ArrayList<>(dataCache);
        if (!lock.validate(stamp)) {
            stamp = lock.readLock();
            try {
                snapshot = new ArrayList<>(dataCache);
            } finally {
                lock.unlockRead(stamp);
                log.debug("Read lock released with stamp: {}", stamp);
            }
        }
        return snapshot;
    }

    protected void refresh() {
        List<T> data = dataFetcher.get();
        long stamp = lock.writeLock();
        try {
            dataCache.clear();
            dataCache.addAll(data);
            log.info("Cache updated, new size: {}", dataCache.size());
        } finally {
            lock.unlockWrite(stamp);
            log.debug("Write lock released with stamp: {}", stamp);
        }
    }
}
