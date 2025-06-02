package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.api.model.ResponseGenre;
import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.exception.GenreNotFoundException;
import com.bondarenko.movieland.mapper.GenreMapper;
import com.bondarenko.movieland.repository.GenreRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreCacheAsideService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;
    private final CopyOnWriteArrayList<Genre> genreCache = new CopyOnWriteArrayList<>();

    public Set<Genre> getGenre() {
        if (genreCache.isEmpty()) {
            throw new GenreNotFoundException();
        }
        log.info("Get all genres from cache, genreCache size: {}", genreCache.size());
        return new HashSet<>(genreCache);
    }

    @PostConstruct
    @Scheduled(fixedDelayString = "${movieland.genre.cache.update.interval}")
    private void cacheLoading() {
        List<Genre> genresFromDb = fetchGenresFromDatabase();
        genreCache.addAll(genresFromDb);
        log.info("Genre cache updated with {} items", genreCache.size());
    }

    private List<Genre> fetchGenresFromDatabase() {
        List<Genre> genres = genreRepository.findAll();
        log.info("Genres successfully fetched from database");
        return new ArrayList<>(genres);
    }
}
