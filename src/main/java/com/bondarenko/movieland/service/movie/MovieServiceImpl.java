package com.bondarenko.movieland.service.movie;

import com.bondarenko.movieland.api.model.MovieSortCriteria;
import com.bondarenko.movieland.api.model.ResponseMovieDTO;
import com.bondarenko.movieland.entity.Movie;
import com.bondarenko.movieland.mapper.MovieMapper;
import com.bondarenko.movieland.repository.MovieRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public List<ResponseMovieDTO> findAllMoviesWithSorting(MovieSortCriteria movieSortCriteria) {

        Optional<Sort.Direction> ratingDirection = Optional.ofNullable(convertRatingDirection(movieSortCriteria.getRatingDirection()));
        Optional<Sort.Direction> priceDirection = Optional.ofNullable(convertPriceDirection(movieSortCriteria.getPriceDirection()));

        Sort sort = Sort.unsorted();
        if (ratingDirection.isPresent()) {
            sort = Sort.by(new Sort.Order(ratingDirection.get(), "rating"));
        } else if (priceDirection.isPresent()) {
            sort = Sort.by(new Sort.Order(priceDirection.get(), "price"));
        }

        List<Movie> movies = movieRepository.findAll(sort);
        return movieMapper.toMovieDTO(movies);

    }

    private Sort.Direction convertRatingDirection(MovieSortCriteria.RatingDirectionEnum ratingDirection) {
        return (ratingDirection == null) ? null : Sort.Direction.valueOf(ratingDirection.getValue());
    }
    private Sort.Direction convertPriceDirection(MovieSortCriteria.PriceDirectionEnum priceDirection) {
        return (priceDirection == null) ? null :  Sort.Direction.valueOf(priceDirection.getValue());
    }

    @Override
    public List<ResponseMovieDTO> getRandomMovies() {
        log.info("Received request to find {} random movies.", 3);

        List<Movie> randomMovies = movieRepository.findRandomMovies(3);
        return movieMapper.toMovieDTO(randomMovies);
    }

    @Override
    public List<ResponseMovieDTO> getMoviesByGenre(int genreId) {
        List<Movie> movies = movieRepository.findByGenresId(genreId);
        return movieMapper.toMovieDTO(movies);
    }


}
