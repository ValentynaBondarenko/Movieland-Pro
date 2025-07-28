package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.service.annotation.CacheService;
import com.bondarenko.movieland.service.genre.GenreService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

@CacheService
@RequiredArgsConstructor
public class GenreCacheProxy implements GenreService {
    private final GenreService genreService;
    private Cache<GenreResponse> genreCache;

    @PostConstruct
    private void init() {
        genreCache = new Cache<>(() -> {
            List<GenreResponse> genre = genreService.findAll();
            return new ArrayList<>(genre);
        });
        genreCache.refresh();
    }

    @Scheduled(fixedDelayString = "${movieland.cache.update.interval}")
    private void refreshCache() {
        genreCache.refresh();
    }

    @Override
    public List<GenreResponse> findByIdIn(List<Long> genreIds) {
        return genreCache.getAll().stream()
                .filter(genre -> genreIds.contains(genre.getId()))
                .toList();
    }

    @Override
    public List<GenreResponse> findByMovieId(Long movieId) {
        return genreService.findByMovieId(movieId);
    }

    @Override
    public List<GenreResponse> findAll() {
        return getAll();
    }

    private List<GenreResponse> getAll() {
        return genreCache.getAll();
    }
}
