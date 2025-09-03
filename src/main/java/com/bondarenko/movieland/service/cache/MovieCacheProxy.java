package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.api.model.FullMovieResponse;
import com.bondarenko.movieland.api.model.MovieRequest;
import com.bondarenko.movieland.api.model.MovieResponse;
import com.bondarenko.movieland.api.model.MovieSortRequest;
import com.bondarenko.movieland.entity.CurrencyType;
import com.bondarenko.movieland.service.annotation.CacheService;
import com.bondarenko.movieland.service.movie.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@CacheService
@RequiredArgsConstructor
public class MovieCacheProxy implements MovieService {
    private final MovieService movieService;
    private final Map<Long, SoftReference<FullMovieResponse>> movieCache = new ConcurrentHashMap<>();

    @Override
    public FullMovieResponse getMovieById(Long movieId, CurrencyType currency) {
        SoftReference<FullMovieResponse> movieFromCache = movieCache.get(movieId);
        FullMovieResponse response = (movieFromCache != null) ? movieFromCache.get() : null;

        if (response != null) {
            log.debug("Cache HIT for movie {}", movieId);
            return response;
        }

        log.debug("Cache MISS for movie {}", movieId);
        response = movieService.getMovieById(movieId, currency);
        movieCache.put(movieId, new SoftReference<>(response));

        return response;
    }

    @Override
    public List<MovieResponse> findAll(MovieSortRequest movieSortRequest) {
        return movieService.findAll(movieSortRequest);
    }

    @Override
    public List<MovieResponse> getRandomMovies() {
        return movieService.getRandomMovies();
    }

    @Override
    public List<MovieResponse> getMoviesByGenre(Long genreId) {
        return movieService.getMoviesByGenre(genreId);
    }

    @Override
    public void saveMovie(MovieRequest movieRequest) {
        movieService.saveMovie(movieRequest);
    }

    @Override
    public FullMovieResponse updateMovie(Long id, MovieRequest movieRequest) {
        return movieService.updateMovie(id, movieRequest);
    }


}
