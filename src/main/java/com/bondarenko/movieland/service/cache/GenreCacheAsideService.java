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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreCacheAsideService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;
//    private final List<ResponseGenre> genreCache = new CopyOnWriteArrayList<>();
//    @Value("${movieland.genre.cache.update.interval}")
//    private Integer cacheUpdateInterval;
//
//    @PostConstruct
//    //Cache Preloading. Fetch all genres on a start
//    public void initialize() {
//        updateCacheBlocking();
//    }

    // not return ref for modify return new ArrayList<>(genreCache);
//    public List<ResponseGenre> getGenres() {
//        if (genreCache.isEmpty()) {
//            throw new GenreNotFoundException();
//        }
//        log.info("Get all genres from cache");
//        return new ArrayList<>(genreCache);
//    }

//    @Scheduled(fixedDelayString = "${movieland.genre.cache.update.interval}")
//    private void updateCache() {
//        try {
//            updateCacheInternal();
//        } catch (Exception e) {
//            log.error("Failed to update genre cache", e);
//        }
//    }
//
//    private void updateCacheBlocking() {
//        try {
//            updateCacheInternal();
//        } catch (Exception e) {
//            log.error("Failed to update genre cache during initialization", e);
//            throw new RuntimeException("Failed to initialize genre cache", e);
//        }
//    }

//    public void updateCacheInternal() {
//        List<ResponseGenre> responseGenres = fetchGenresFromDatabase();
//        genreCache.clear();
//        genreCache.addAll(responseGenres);
//        log.info("Genre cache updated successfully");
//    }

    public List<ResponseGenre> fetchGenresFromDatabase() {
        List<Genre> genres = genreRepository.findAll();
        List<ResponseGenre> responseGenres = Optional.of(genres)
                .map(genreMapper::toGenreResponse)
                .orElseThrow(GenreNotFoundException::new);
        log.info("Fetched genres from database");
        return new ArrayList<>(responseGenres);
    }
}
