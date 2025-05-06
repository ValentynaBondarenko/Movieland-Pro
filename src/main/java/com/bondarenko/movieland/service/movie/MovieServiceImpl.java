package com.bondarenko.movieland.service.movie;

import com.bondarenko.movieland.api.model.MovieRequest;
import com.bondarenko.movieland.api.model.MovieSortCriteria;
import com.bondarenko.movieland.api.model.ResponseFullMovie;
import com.bondarenko.movieland.api.model.ResponseMovie;
import com.bondarenko.movieland.entity.Country;
import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.entity.Movie;
import com.bondarenko.movieland.exception.CountryNotFoundException;
import com.bondarenko.movieland.exception.GenreNotFoundException;
import com.bondarenko.movieland.exception.MovieNotFoundException;
import com.bondarenko.movieland.mapper.MovieMapper;
import com.bondarenko.movieland.repository.CountryRepository;
import com.bondarenko.movieland.repository.GenreRepository;
import com.bondarenko.movieland.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final CountryRepository countryRepository;
    private final MovieMapper movieMapper;
    @Value("${movieland.movie.random.limit}")
    private int limit;
    private static final String RATING = "rating";
    private static final String PRICE = "price";

    @Transactional
    @Override
    public List<ResponseMovie> findAll(MovieSortCriteria movieSortCriteria) {
        List<Movie> movies;
        if (movieSortCriteria != null) {
            Sort sort = buildSort(movieSortCriteria);
            movies = movieRepository.findAll(sort);
        } else {
            movies = movieRepository.findAll();
        }
        return movieMapper.toMovieResponse(movies);
    }

    @Transactional
    @Override
    public List<ResponseMovie> getRandomMovies() {
        List<Movie> randomMovies = movieRepository.findRandomMovies(limit);
        System.out.println(randomMovies);
        return movieMapper.toMovieResponse(randomMovies);
    }

    @Override
    public List<ResponseMovie> getMoviesByGenre(int genreId) {
        List<Movie> movies = movieRepository.findByGenresId(genreId);
        return movieMapper.toMovieResponse(movies);
    }

    @Override
    @Transactional
    public ResponseFullMovie getMovieById(Integer movieId) {
        Movie movie = movieRepository.getMovieById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(String.format("Movie not found with ID: %d", movieId)));

        return movieMapper.toFullMovie(movie);
    }

    @Override
    @Transactional
    public void saveMovie(MovieRequest movieRequest) {
        Movie movie = movieMapper.toMovie(movieRequest);

        if (movie.getRating() == null) {
            movie.setRating(BigDecimal.ZERO);
        }
        enrichMovieWithGenresAndCountries(movieRequest, movie);

        movieRepository.save(movie);

        log.info("Successfully saved movie {} to the database", movie);
    }

    @Transactional
    @Override
    public ResponseFullMovie updateMovie(Integer id, MovieRequest movieRequest) {
        Movie movie = movieRepository.findById(id.longValue())
                .orElseThrow(() -> new MovieNotFoundException(String.format("Movie not found with ID: %d", id)));

        movie.setNameUkrainian(movieRequest.getNameUkrainian());
        movie.setNameNative(movieRequest.getNameNative());
        movie.setPoster(movieRequest.getPicturePath());
        List<Genre> genres = mapGenresIdToGenres(movieRequest);
        movie.setGenres(genres);
        List<Country> countries = mapCountryIdToCountries(movieRequest);
        movie.setCountries(countries);

        movieRepository.save(movie);

        ResponseFullMovie response = movieMapper.toMovieResponse(movie);
        log.info("Successfully updated movie id {} to the database", movie.getId());
        return response;
    }

    private void enrichMovieWithGenresAndCountries(MovieRequest movieRequest, Movie movie) {
        List<Country> countries = mapCountryIdToCountries(movieRequest);
        List<Genre> genres = mapGenresIdToGenres(movieRequest);
        movie.setGenres(new ArrayList<>(genres));
        movie.setCountries(new ArrayList<>(countries));
    }

    private List<Country> mapCountryIdToCountries(MovieRequest movieRequest) {


        return movieRequest.getCountries().stream()
                .map(countryId -> countryRepository.findById(Long.valueOf(countryId))
                        .orElseThrow(() -> new CountryNotFoundException("Can't found country by id: " + countryId)))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private List<Genre> mapGenresIdToGenres(MovieRequest movieRequest) {
        return movieRequest.getGenres().stream()
                .map(genreId -> genreRepository.findById(Long.valueOf(genreId))
                        .orElseThrow(() -> new GenreNotFoundException("Can't find genre by id: " + genreId)))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Sort buildSort(MovieSortCriteria movieSortCriteria) {
        Optional<Sort.Direction> ratingDirection = Optional.ofNullable(convertRatingDirection(movieSortCriteria.getRatingDirection()));
        Optional<Sort.Direction> priceDirection = Optional.ofNullable(convertPriceDirection(movieSortCriteria.getPriceDirection()));

        if (ratingDirection.isPresent()) {
            return Sort.by(new Sort.Order(ratingDirection.get(), RATING));
        } else if (priceDirection.isPresent()) {
            return Sort.by(new Sort.Order(priceDirection.get(), PRICE));
        }
        return Sort.unsorted();
    }

    private Sort.Direction convertRatingDirection(MovieSortCriteria.RatingDirectionEnum ratingDirection) {
        return (ratingDirection == null) ? null : Sort.Direction.valueOf(ratingDirection.getValue());
    }

    private Sort.Direction convertPriceDirection(MovieSortCriteria.PriceDirectionEnum priceDirection) {
        return (priceDirection == null) ? null : Sort.Direction.valueOf(priceDirection.getValue());
    }
}
