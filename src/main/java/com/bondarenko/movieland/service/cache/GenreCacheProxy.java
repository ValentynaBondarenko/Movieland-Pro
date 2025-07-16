package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.exception.GenreNotFoundException;
import com.bondarenko.movieland.mapper.GenreMapper;
import com.bondarenko.movieland.service.annotation.CacheService;
import com.bondarenko.movieland.service.genre.GenreService;
import com.bondarenko.movieland.service.genre.GenreServiceImpl;
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
    private final GenreMapper genreMapper;

    private Cache<Genre> genreCache;

    @PostConstruct
    private void init() {
        genreCache = new Cache<>(() -> {
            Set<GenreResponse> all = genreService.findAll();
            Set<Genre> genre = genreMapper.toGenre(all);
            return new ArrayList<>(genre);
        });
        genreCache.refresh();
    }

    @Scheduled(fixedDelayString = "${movieland.cache.update.interval}")
    private void refreshCache() {
        genreCache.refresh();
    }

    @Override
    public Set<Genre> findByIdIn(Set<Long> genreIds) {
        return genreCache.getAll().stream()
                .filter(genre -> genreIds.contains(genre.getId()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Genre> findByMovieId(Long movieId) {
        Set<Genre> genres = genreService.findByMovieId(movieId);
        genreCache.addIfNotPresent(new ArrayList<>(genres));
        return genres;
    }

    @Override
    public Set<GenreResponse> findAll() {
        return genreMapper.toGenreResponse(new HashSet<>(genreCache.getAll()));
    }

    @Override
    public Genre getGenreById(Long id) {
        return genreCache.getAll().stream()
                .filter(genre -> genre.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new GenreNotFoundException("Genre not found with id: " + id));
    }

    private List<Genre> getAll() {
        return genreMapper.toGenre(findAll()).stream().toList();
    }
}
