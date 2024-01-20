package com.bondarenko.movieland.service.movie;

import com.bondarenko.movieland.api.model.MovieSortCriteria;
import com.bondarenko.movieland.api.model.ResponseFullMovie;
import com.bondarenko.movieland.api.model.ResponseMovie;
import com.bondarenko.movieland.entity.Movie;
import com.bondarenko.movieland.mapper.MovieMapper;
import com.bondarenko.movieland.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    @Value("${movieland.movie.random.limit}")
    private int limit;
    private static final String RATING = "rating";
    private static final String PRICE = "price";

    @Override
    public List<ResponseMovie> findAllMovies(MovieSortCriteria movieSortCriteria) {
        log.info("Received request to find all movies.");
        List<Movie> movies;
        if (movieSortCriteria != null) {
            Sort sort = buildSort(movieSortCriteria);
            movies = movieRepository.findAll(sort);
        } else {
            movies = movieRepository.findAll();
        }
        return movieMapper.toMovieDTO(movies);
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

    @Override
    public List<ResponseMovie> getRandomMovies() {
        log.info("Received request to find {} random movies.", limit);

        List<Movie> randomMovies = movieRepository.findRandomMovies(limit);
        return movieMapper.toMovieDTO(randomMovies);
    }

    @Override
    public List<ResponseMovie> getMoviesByGenre(int genreId) {
        List<Movie> movies = movieRepository.findByGenresId(genreId);
        return movieMapper.toMovieDTO(movies);
    }

    @Override
    @Transactional
    public ResponseFullMovie getMovieById(Integer movieId) {
        Movie movie = movieRepository.getMovieById(movieId);
//        ResponseFullMovie fullMovie = movieMapper.toFullMovie(movie);
        return null;
    }

    private Sort.Direction convertRatingDirection(MovieSortCriteria.RatingDirectionEnum ratingDirection) {
        return (ratingDirection == null) ? null : Sort.Direction.valueOf(ratingDirection.getValue());
    }

    private Sort.Direction convertPriceDirection(MovieSortCriteria.PriceDirectionEnum priceDirection) {
        return (priceDirection == null) ? null : Sort.Direction.valueOf(priceDirection.getValue());
    }
}
