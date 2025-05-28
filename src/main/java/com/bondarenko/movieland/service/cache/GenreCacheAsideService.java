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

    public List<ResponseGenre> getGenre() {
        if (genreCache.isEmpty()) {
            throw new GenreNotFoundException();
        }
        log.info("Get all genres from cache, genreCache size: {}", genreCache.size());
        return new ArrayList<>(genreCache);
    }

    @PostConstruct
    @Scheduled(fixedDelayString = "${movieland.genre.cache.update.interval}")
    private void cacheLoading() {
        List<ResponseGenre> genresFromDb = fetchGenresFromDatabase();
        genreCache.addAll(genresFromDb);
        log.info("Genre cache updated with {} items", genreCache.size());
    }

    private List<ResponseGenre> fetchGenresFromDatabase() {
        List<Genre> genres = genreRepository.findAll();
        List<ResponseGenre> responseGenres = Optional.of(genres)
                .map(genreMapper::toGenreResponse)
                .orElseThrow(GenreNotFoundException::new);
        log.info("Genres successfully fetched from database");
        return new ArrayList<>(responseGenres);
    }
}
