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
    private CustomCache<Genre> genreCache;

    @PostConstruct
    public void init() {
        genreCache = new CustomCache<>(genreRepository::findAll);
        genreCache.refresh();
    }

    @Scheduled(fixedDelayString = "${movieland.genre.cache.update.interval}")
    public void refreshCache() {
        genreCache.refresh();
    }

    public Set<Genre> getGenres() {
        return genreCache.getAll();
    }
}
