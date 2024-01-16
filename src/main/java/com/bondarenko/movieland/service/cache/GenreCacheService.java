package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class GenreCacheService {
    private final GenreRepository genreRepository;
    private final CopyOnWriteArrayList<Genre> genreCache = new CopyOnWriteArrayList<>();
    private final AtomicLong lastCacheUpdateTime = new AtomicLong(0);

    @Value("${movieland.genre.cache.update.interval}")
    private Integer cacheUpdateInterval;


    public List<Genre> getGenres() {
        long currentTime = System.currentTimeMillis();
        long lastUpdateTime = lastCacheUpdateTime.get();

        if (genreCache.isEmpty() || (currentTime - lastUpdateTime >= cacheUpdateInterval)) {
            List<Genre> genres = genreRepository.findAll();
            genreCache.addAllAbsent(genres);
            lastCacheUpdateTime.set(currentTime);
        }

        return genreCache.stream().toList();
    }

}
