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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreCacheAsideService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;
    private final CopyOnWriteArrayList<ResponseGenre> genreCache = new CopyOnWriteArrayList<>();

    @Value("${movieland.genre.cache.update.interval}")
    private Integer cacheUpdateInterval;

    /**
     * Cache preloading on application startup.
     * ⚠️ This may increase startup time slightly.
     */
    @PostConstruct
    private void initialize() {
        try {
            log.info("Initializing genre cache...");
            cacheLoading();
        } catch (Exception e) {
            log.error("Failed to update genre cache during initialization", e);
            throw new RuntimeException("Failed to initialize genre cache", e);

        }
    }

    public List<ResponseGenre> getGenre() {
        if (genreCache.isEmpty()) {
            throw new GenreNotFoundException();
        }
        log.info("Get all genres from cache, genreCache size: {}", genreCache.size());
        return new ArrayList<>(genreCache);
    }

    @Scheduled(fixedDelayString = "${movieland.genre.cache.update.interval}")
    private void cacheLoading() {
        List<ResponseGenre> genresFromDb = fetchGenresFromDatabase();
        genreCache.clear();
        genreCache.addAll(genresFromDb);
        log.info("Genre cache updated with {} items", genreCache.size());
    }

    private List<ResponseGenre> fetchGenresFromDatabase() {
        List<Genre> genres = genreRepository.findAll();
        List<ResponseGenre> responseGenres = Optional.of(genres)
                .map(genreMapper::toGenreResponse)
                .orElseThrow(GenreNotFoundException::new);
        log.info("Fetched genres from database");
        return new ArrayList<>(responseGenres);
    }
}
