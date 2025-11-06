package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.mapper.GenreMapper;
import com.bondarenko.movieland.service.annotation.CacheService;
import com.bondarenko.movieland.service.genre.GenreService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@CacheService
@RequiredArgsConstructor
public class GenreCacheProxy implements GenreService {
    private final GenreService genreService;
    private Cache<GenreResponse> genreCache;
    private final GenreMapper genreMapper;

    @PostConstruct
    private void init() {
        genreCache = new Cache<>(() -> {
            List<GenreResponse> genre = genreService.findAll();
            return Collections.unmodifiableList(new ArrayList<>(genre));
        });
        genreCache.refresh();
        log.info("Country cache initialized, size= {}", genreCache.getAll().size());
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
    public List<Genre> findById(List<Long> genreIds) {
        List<GenreResponse> list = genreCache.getAll().stream()
                .filter(genre -> genreIds.contains(genre.getId()))
                .toList();
        return genreMapper.toGenre(list);
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
