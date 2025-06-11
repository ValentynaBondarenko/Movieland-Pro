package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.repository.GenreRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class GenreCache {
    private final GenreRepository genreRepository;
    private Cache<Genre> genreCache;

    public Set<Genre> getGenres() {
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
