package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.api.model.ResponseGenre;
import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.exception.GenreNotFoundException;
import com.bondarenko.movieland.mapper.GenreMapper;
import com.bondarenko.movieland.repository.GenreRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class GenreCacheAsideService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;
    private final List<ResponseGenre> genreCache = new CopyOnWriteArrayList<>();
    @Value("${movieland.genre.cache.update.interval}")
    private Integer cacheUpdateInterval;

    @PostConstruct
    public void initialize() {
        System.out.println("Init mehod start ////////////");
        startCacheUpdateScheduler();
    }

    public List<ResponseGenre> getGenres() {
        log.info("Get all genres from cache");
        return new ArrayList<>(genreCache);
    }

    public Integer getCacheUpdateInterval() {
        return cacheUpdateInterval;
    }

    private void updateCache() {
        try {
            List<Genre> genres = genreRepository.findAll();

            List<ResponseGenre> responseGenres = Optional.of(genres)
                    .map(genreMapper::toGenreResponse)
                    .orElseThrow(GenreNotFoundException::new);

            genreCache.clear();
            genreCache.addAll(responseGenres);
            log.info("Genre cache updated successfully");
        } catch (Exception e) {
            log.error("Failed to update genre cache", e);
        }
    }

    private void startCacheUpdateScheduler() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleWithFixedDelay(this::updateCache, 0, cacheUpdateInterval, TimeUnit.MILLISECONDS);

    }
}
