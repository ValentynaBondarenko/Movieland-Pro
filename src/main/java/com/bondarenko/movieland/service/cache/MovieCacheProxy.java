package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.api.model.*;
import com.bondarenko.movieland.entity.CurrencyType;
import com.bondarenko.movieland.service.annotation.CacheService;
import com.bondarenko.movieland.service.currency.CurrencyService;
import com.bondarenko.movieland.service.movie.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@CacheService
@RequiredArgsConstructor
public class MovieCacheProxy implements MovieService {
    private final MovieService movieService;
    private final CurrencyService currencyService;
    private SoftReferenceCache<Long, FullMovieResponse> movieCache;

    @Override
    public FullMovieResponse getMovieById(Long movieId, CurrencyType currency) {
        ensureCacheInitialized();

        FullMovieResponse movie = movieCache.get(movieId);
        return applyCurrency(movie, currency);
    }

    private synchronized void ensureCacheInitialized() {
        if (movieCache == null) {
            log.info("Initializing SoftReference movie cache...");
            movieCache = new SoftReferenceCache<>(id -> {
                log.info("Loading movie {} into cache", id);
                return movieService.getMovieById(id, null);
            });
        }
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

    private FullMovieResponse applyCurrency(FullMovieResponse movie, CurrencyType currency) {
        if (currency == null || currency == CurrencyType.UAH) {
            return movie;
        }

        FullMovieResponse converted = deepCopy(movie);
        Double moviePrice = movie.getPrice();

        if (moviePrice != null) {
            BigDecimal convertedPrice = currencyService.convertCurrency(
                    BigDecimal.valueOf(moviePrice), currency
            );
            converted.setPrice(convertedPrice.doubleValue());
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

        copy.setGenres(original.getGenres().stream().map(genre -> {
            GenreResponse genreResponse = new GenreResponse();
            genreResponse.setId(genre.getId());
            genreResponse.setName(genre.getName());
            return genreResponse;
        }).toList());

        copy.setCountries(original.getCountries().stream().map(country -> {
            CountryResponse countryResponse = new CountryResponse();
            countryResponse.setId(country.getId());
            countryResponse.setName(country.getName());
            return countryResponse;
        }).toList());

        copy.setReviews(original.getReviews().stream().map(review -> {
            ReviewResponse reviewResponse = new ReviewResponse();
            reviewResponse.setId(review.getId());
            reviewResponse.setText(review.getText());

            UserIdResponse userCopy = new UserIdResponse();
            if (review.getUser() != null) {
                userCopy.setId(review.getUser().getId());
                userCopy.setNickname(review.getUser().getNickname());
            }
            reviewResponse.setUser(userCopy);
            return reviewResponse;
        }).toList());

        return copy;
    }

    public void clearCacheForTests() {
        if (movieCache != null) {
            movieCache.clear();
        }
    }
}
