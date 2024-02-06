package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
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
        long currentInterval = currentTime - lastUpdateTime;

        if (genreCache.isEmpty() || (currentInterval >= cacheUpdateInterval)) {
            log.info("Get all genres from database");
            List<Genre> genres = genreRepository.findAll();

            genreCache.addAllAbsent(genres);

            lastCacheUpdateTime.set(currentTime);
        }
        log.info("Get all genres from cache");
        return new ArrayList(genreCache);
    }

     public void clearCache(){
        genreCache.clear();
     }

}
