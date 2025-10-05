package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.api.model.*;
import com.bondarenko.movieland.entity.CurrencyType;
import com.bondarenko.movieland.service.annotation.CacheService;
import com.bondarenko.movieland.service.currency.CurrencyService;
import com.bondarenko.movieland.service.movie.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Slf4j
@CacheService
@RequiredArgsConstructor
public class MovieCacheProxy implements MovieService {
    private final MovieService movieService;
    private final CurrencyService currencyService;
    private final Map<Long, SoftReference<FullMovieResponse>> movieCache = new ConcurrentHashMap<>();

    @Override
    public FullMovieResponse getMovieById(Long movieId, CurrencyType currency) {
        SoftReference<FullMovieResponse> movieFromCache = movieCache.get(movieId);
        FullMovieResponse cachedMovie = (movieFromCache != null) ? movieFromCache.get() : null;

        if (cachedMovie != null) {
            log.debug("Cache HIT for movie {}", movieId);
            return applyCurrency(cachedMovie, currency);
        }

        log.debug("Cache MISS for movie {}", movieId);
        FullMovieResponse movie = movieService.getMovieById(movieId, null);
        movieCache.put(movieId, new SoftReference<>(movie));

        return applyCurrency(movie, currency);
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
        FullMovieResponse updateMovie = movieService.updateMovie(id, movieRequest);
        movieCache.put(id, new SoftReference<>(updateMovie));

        log.debug("Cache UPDATED for movie with id {}", id);

        return updateMovie;
    }

    private FullMovieResponse applyCurrency(FullMovieResponse movie, CurrencyType currency) {
        if (currency == null || currency == CurrencyType.UAH) {
            return movie;
        }
        FullMovieResponse converted = deepCopy(movie);
        Double moviePrice = movie.getPrice();

        if (moviePrice != null) {
            BigDecimal price = currencyService.convertCurrency(BigDecimal.valueOf(moviePrice), currency);
            converted.setPrice(price.doubleValue());
        } else {
            converted.setPrice(null);
        }

        return converted;
    }

    private FullMovieResponse deepCopy(FullMovieResponse original) {
        FullMovieResponse copy = new FullMovieResponse();
        copy.setId(original.getId());
        copy.setNameUkrainian(original.getNameUkrainian());
        copy.setNameNative(original.getNameNative());
        copy.setYearOfRelease(original.getYearOfRelease());
        copy.setDescription(original.getDescription());
        copy.setRating(original.getRating());
        copy.setPrice(original.getPrice());
        copy.setPicturePath(original.getPicturePath());

        copy.setGenres(original.getGenres().stream().map(g -> {
            GenreResponse gr = new GenreResponse();
            gr.setId(g.getId());
            gr.setName(g.getName());
            return gr;
        }).toList());

        copy.setCountries(original.getCountries().stream().map(c -> {
            CountryResponse cr = new CountryResponse();
            cr.setId(c.getId());
            cr.setName(c.getName());
            return cr;
        }).toList());

        copy.setReviews(original.getReviews().stream().map(r -> {
            ReviewResponse rr = new ReviewResponse();
            rr.setId(r.getId());
            rr.setText(r.getText());

            UserIdResponse userCopy = new UserIdResponse();
            if (r.getUser() != null) {
                userCopy.setId(r.getUser().getId());
                userCopy.setNickname(r.getUser().getNickname());
            }
            rr.setUser(userCopy);
            return rr;
        }).toList());

        return copy;
    }
}
