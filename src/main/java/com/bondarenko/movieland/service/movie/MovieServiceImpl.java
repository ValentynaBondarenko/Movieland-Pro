package com.bondarenko.movieland.service.movie;

import com.bondarenko.movieland.api.model.FullMovieResponse;
import com.bondarenko.movieland.api.model.MovieRequest;
import com.bondarenko.movieland.api.model.MovieResponse;
import com.bondarenko.movieland.api.model.MovieSortRequest;
import com.bondarenko.movieland.entity.CurrencyType;
import com.bondarenko.movieland.entity.Movie;
import com.bondarenko.movieland.exception.MovieNotFoundException;
import com.bondarenko.movieland.mapper.MovieMapper;
import com.bondarenko.movieland.repository.MovieRepository;
import com.bondarenko.movieland.service.currency.CurrencyServiceImpl;
import com.bondarenko.movieland.service.enrichment.EnrichmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final EnrichmentService enrichmentService;
    private final MovieMapper movieMapper;
    private final CurrencyServiceImpl converter;
    @Value("${movieland.movie.random.limit}")
    private int limit;
    private static final String RATING = "rating";
    private static final String PRICE = "price";

    @Transactional
    @Override
    public List<MovieResponse> findAll(MovieSortRequest movieSortRequest) {
        List<Movie> movies;
        if (movieSortRequest != null) {
            Sort sort = buildSort(movieSortRequest);
            movies = movieRepository.findAll(sort);
        } else {
            movies = movieRepository.findAll();
        }
        return movieMapper.toMovieResponse(movies);
    }

    @Transactional
    @Override
    public List<MovieResponse> getRandomMovies() {
        List<Movie> randomMovies = movieRepository.findRandomMovies(limit);
        log.info("Random movies count: {}", randomMovies.size());
        return movieMapper.toMovieResponse(randomMovies);
    }

    @Override
    public List<MovieResponse> getMoviesByGenre(Long genreId) {
        List<Movie> movies = movieRepository.findByGenresId(genreId);
        return movieMapper.toMovieResponse(movies);
    }

    /**
     * [ getMovieById() ]  <-- @Transactional (Main transaction Begin)
     * |
     * |---> movieRepository.getMovieById()
     * |          (Main Tx: Begin → Query → Commit → (stop Main Tx))
     * |
     * |---> enrichMovie(movie)
     * |      |
     * |      |---> genreService.findByMovieId()
     * |      |          (@Transactional(REQUIRES_NEW): Begin → Query → Commit → Close)
     * |      |
     * |      |---> countryService.findByMovieId()
     * |      |          (@Transactional(REQUIRES_NEW): Begin → Query → Commit → Close)
     * |      |
     * |      |---> reviewService.findByMovieId()
     * |                 (@Transactional(REQUIRES_NEW): Begin → Query → Commit → Close)
     * |
     * |--->  enriched Movie
     * (Main Tx: Resume → Commit → Close)
     */

    @Override
    @Transactional(readOnly = true)
    public FullMovieResponse getMovieById(Long movieId, CurrencyType currency) {
        Movie movie = movieRepository.getMovieById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(String.format("Movie not found with ID: %d", movieId)));
        BigDecimal correctMoviePrice = converter.convertCurrency(movie.getPrice(), currency);
        //todo
        movie.setPrice(correctMoviePrice);
        // For example, not all data is stored in a single database,
        // and it may not be possible to fetch everything within one transaction.
        MovieRequest fullMovie = movieMapper.toMovieRequest(movie);

        enrichmentService.enrichMovie(fullMovie);
        return movieMapper.toFullMovie(movie);
    }

    @Override
    @Transactional
    public void saveMovie(MovieRequest movieRequest) {
        enrichmentService.enrichMovie(movieRequest);

        Movie movie = movieMapper.toMovie(movieRequest);

        movieRepository.save(movie);

        log.info("Successfully saved movie {} to the database", movie);
    }

    @Transactional
    @Override
    public FullMovieResponse updateMovie(Long id, MovieRequest movieRequest) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(String.format("Movie not found with ID: %d", id)));

        movie.setNameUkrainian(movieRequest.getNameUkrainian())
                .setNameNative(movieRequest.getNameNative())
                .setYearOfRelease(movieRequest.getYearOfRelease())
                .setDescription(movieRequest.getDescription())
                .setPrice(BigDecimal.valueOf(Objects.requireNonNull(movieRequest.getPrice())))
                .setRating(BigDecimal.valueOf(Objects.requireNonNull(movieRequest.getRating())))
                .setPoster(movieRequest.getPicturePath());

        enrichmentService.enrichMovie(movieRequest);

        movieRepository.save(movie);

        FullMovieResponse response = movieMapper.toMovieResponse(movie);
        log.info("Successfully updated movie id {} to the database", movie.getId());
        return response;
    }

    private Sort buildSort(MovieSortRequest movieSortRequest) {
        //QueryBuilder???????
        Optional<Sort.Direction> ratingDirection = Optional.ofNullable(convertRatingDirection(movieSortRequest.getRatingDirection()));
        Optional<Sort.Direction> priceDirection = Optional.ofNullable(convertPriceDirection(movieSortRequest.getPriceDirection()));

        if (ratingDirection.isPresent()) {
            //not a string?
            return Sort.by(new Sort.Order(ratingDirection.get(), RATING));
        } else if (priceDirection.isPresent()) {
            return Sort.by(new Sort.Order(priceDirection.get(), PRICE));
        }
        return Sort.unsorted();
    }

    private Sort.Direction convertRatingDirection(MovieSortRequest.RatingDirectionEnum ratingDirection) {
        return (ratingDirection == null) ? null : Sort.Direction.valueOf(ratingDirection.getValue());
    }

    private Sort.Direction convertPriceDirection(MovieSortRequest.PriceDirectionEnum priceDirection) {
        return (priceDirection == null) ? null : Sort.Direction.valueOf(priceDirection.getValue());
    }

}
