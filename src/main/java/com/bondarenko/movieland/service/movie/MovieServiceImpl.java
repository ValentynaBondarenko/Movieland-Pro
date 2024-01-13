package com.bondarenko.movieland.service.movie;

import com.bondarenko.movieland.api.model.ResponseMovieDTO;
import com.bondarenko.movieland.entity.Movie;
import com.bondarenko.movieland.entity.SortDirection;
import com.bondarenko.movieland.mapper.MovieMapper;
import com.bondarenko.movieland.repository.MovieRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    @Override
    public List<ResponseMovieDTO> findAllMovies() {
        log.info("Received request to find all movies.");

        List<Movie> movies = movieRepository.findAll();
        return movieMapper.toMovieDTO(movies);
    }

    @Override
    public List<ResponseMovieDTO> findAllMoviesWithSorting(String rating, String price) {
        log.info("Received request to find all movies with sorting: rating={}, price={}", rating, price);

        SortDirection ratingDirection = SortDirection.fromString(rating);
        SortDirection priceDirection = SortDirection.fromString(price);

        List<Movie> movies = switch (ratingDirection) {
            case DESC -> getMoviesByDESC(priceDirection);
            case ASC -> getMoviesByASC(priceDirection);
            default -> movieRepository.findAll();
        };

        return movieMapper.toMovieDTO(movies);
    }

    @Override
    public List<ResponseMovieDTO> getRandomMovies() {
        log.info("Received request to find {} random movies.", 3);

        List<Movie> randomMovies = movieRepository.findRandomMovies(3);
        return movieMapper.toMovieDTO(randomMovies);
    }

    @Override
    public List<ResponseMovieDTO> getMoviesByGenre(int genreId) {
        List<Movie> movies = movieRepository.findByGenres_Id(genreId);
        return movieMapper.toMovieDTO(movies);
    }

    private List<Movie> getMoviesByDESC(SortDirection priceDirection) {
        return switch (priceDirection) {
            case ASC -> movieRepository.findAllByOrderByRatingDescPriceAsc();
            case DESC -> movieRepository.findAllByOrderByRatingDescPriceDesc();
            default -> movieRepository.findAllByOrderByRatingDesc();
        };
    }

    private List<Movie> getMoviesByASC(SortDirection priceDirection) {
        return switch (priceDirection) {
            case ASC -> movieRepository.findAllByOrderByRatingAscPriceAsc();
            case DESC -> movieRepository.findAllByOrderByRatingAscPriceDesc();
            default -> movieRepository.findAllByOrderByRatingAsc();
        };
    }
}
