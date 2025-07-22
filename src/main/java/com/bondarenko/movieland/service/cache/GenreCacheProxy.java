package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.exception.GenreNotFoundException;
import com.bondarenko.movieland.service.annotation.CacheService;
import com.bondarenko.movieland.service.genre.GenreService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@CacheService
@RequiredArgsConstructor
public class GenreCacheProxy implements GenreService {
    private final GenreService genreService;
    private Cache<GenreResponse> genreCache;

    @PostConstruct
    private void init() {
        genreCache = new Cache<>(() -> {
            Set<GenreResponse> genre = genreService.findAll();
            return new ArrayList<>(genre);
        });
        genreCache.refresh();
    }

    @Scheduled(fixedDelayString = "${movieland.cache.update.interval}")
    private void refreshCache() {
        genreCache.refresh();
    }

    @Override
    public Set<GenreResponse> findByIdIn(Set<Long> genreIds) {
        return genreCache.getAll().stream()
                .filter(genre -> genreIds.contains(genre.getId()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<GenreResponse> findByMovieId(Long movieId) {
        Set<GenreResponse> genres = genreService.findByMovieId(movieId);
        genreCache.addIfNotPresent(new ArrayList<>(genres));
        return genres;
    }

    @Override
    public Set<GenreResponse> findAll() {
        return new HashSet<>(getAll());
    }

    @Override
    public GenreResponse getGenreById(Long id) {
        return genreCache.getAll().stream()
                .filter(genre -> genre.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new GenreNotFoundException("Genre not found with id: " + id));
    }

    private List<GenreResponse> getAll() {
        return genreCache.getAll();
    }
}
