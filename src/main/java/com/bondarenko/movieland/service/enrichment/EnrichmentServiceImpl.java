package com.bondarenko.movieland.service.enrichment;

import com.bondarenko.movieland.api.model.CountryResponse;
import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.api.model.MovieRequest;
import com.bondarenko.movieland.entity.Country;
import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.entity.Movie;
import com.bondarenko.movieland.entity.Review;
import com.bondarenko.movieland.mapper.CountryMapper;
import com.bondarenko.movieland.mapper.GenreMapper;
import com.bondarenko.movieland.service.country.CountryService;
import com.bondarenko.movieland.service.genre.GenreService;
import com.bondarenko.movieland.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrichmentServiceImpl implements EnrichmentService {
    private final CountryService countryService;
    private final GenreService genreService;
    private final ReviewService reviewService;
    private final GenreMapper genreMapper;
    private final CountryMapper countryMapper;


    @Override
    public Movie enrichMovie(Movie movie, MovieRequest movieRequest) {
        Set<Long> genreIds = movieRequest.getGenres().stream()
                .map(GenreResponse::getId)
                .collect(Collectors.toSet());
        Set<GenreResponse> genresDTO = genreService.findByIdIn(genreIds);

        Set<Long> countryIds = movieRequest.getCountries().stream()
                .map(CountryResponse::getId)
                .collect(Collectors.toSet());

        Set<CountryResponse> countriesDTO = countryService.findByIdIn(countryIds);
        Set<Country> countries = countryMapper.toCountries(countriesDTO);
        Set<Genre> genres = genreMapper.toGenre(genresDTO);

        return movie.setGenres(genres)
                .setCountries(countries);
    }

    /**
     * Enriches the given {@link Movie} object with related entities: genres, countries, and reviews.
     * <p>
     * This method fetches associated data from other services, which may connect to
     * DIFFERENT databases or external data sources.
     * genreService.findByMovieId(id);--new transaction
     * countryService.findByMovieId(id);--new transaction
     * reviewService.findByMovieId(id);--new transaction
     */
    @Override
    public void enrichMovie(Movie movie) {
        if (movie == null) {
            return;
        }
        Long id = movie.getId();
        if (movie.getGenres() == null || movie.getGenres().isEmpty()) {
            Set<GenreResponse> genres = genreService.findByMovieId(id);

            movie.setGenres(genreMapper.toGenre(genres));
        }

        if (movie.getCountries() == null || movie.getCountries().isEmpty()) {
            Set<CountryResponse> countries = countryService.findByMovieId(id);
            movie.setCountries(countryMapper.toCountries(countries));
        }

        if (movie.getReviews() == null || movie.getReviews().isEmpty()) {
            Set<Review> reviews = reviewService.findByMovieId(id);
            movie.setReviews(reviews.stream().toList());
        }
    }

}