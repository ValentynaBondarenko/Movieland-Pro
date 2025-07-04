package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.repository.GenreRepository;
import com.bondarenko.movieland.service.annotation.CacheService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;


@CacheService
@RequiredArgsConstructor
public class GenreCache {
    private final GenreRepository genreRepository;
    private Cache<Genre> genreCache;

    public List<Genre> getGenres() {
        return genreCache.getAll();
    }

    @PostConstruct
    private void init() {
        genreCache = new Cache<>(genreRepository::findAll);
        genreCache.refresh();
    }

    @Scheduled(fixedDelayString = "${movieland.cache.update.interval}")
    private void refreshCache() {
        genreCache.refresh();
    }

}
