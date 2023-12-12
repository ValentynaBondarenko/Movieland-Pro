package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.repository.GenreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
@AllArgsConstructor
public class GenreCacheService {
    private GenreRepository genreRepository;
    private final ConcurrentHashMap<String, List<Genre>> genreCache = new ConcurrentHashMap<>();
    private final AtomicLong lastCacheUpdateTime = new AtomicLong(0);
    private final long cacheUpdateInterval = 4 * 60 * 60 * 1000;

    public List<Genre> getGenres() {
//        long currentTime = System.currentTimeMillis();
//        long lastUpdateTime = lastCacheUpdateTime.get();

        //  if (genreCache.isEmpty() || (currentTime - lastUpdateTime >= cacheUpdateInterval)) {
        if (genreCache.isEmpty()) {
            List<Genre> genres = genreRepository.findAll();
            genreCache.put("genres", new CopyOnWriteArrayList<>(genres));
            // lastCacheUpdateTime.set(currentTime);
        }

        return genreCache.get("genres");
    }

}
