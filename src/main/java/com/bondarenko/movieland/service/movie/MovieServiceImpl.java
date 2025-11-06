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
import com.bondarenko.movieland.repository.MovieSpecifications;
import com.bondarenko.movieland.service.currency.CurrencyService;
import com.bondarenko.movieland.service.enrichment.EnrichmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final EnrichmentService enrichmentService;
    private final MovieMapper movieMapper;
    private final CurrencyService converter;
    @Value("${movieland.movie.random.limit}")
    private int limit;
    private static final String RATING = "rating";
    private static final String PRICE = "price";

    @Transactional
    @Override
    public List<MovieResponse> findAll(MovieSortRequest movieSortRequest) {
        validate(movieSortRequest);
        if (movieSortRequest == null) {
            List<Movie> movies = movieRepository.findAll();
            return movieMapper.toMovieResponse(movies);
        }

        Sort sort = buildSort(movieSortRequest);

        if (isPaginationRequested(movieSortRequest)) {
            return findAllWithPagination(movieSortRequest, sort);
        } else {
            return findAllWithoutPagination(movieSortRequest, sort);
        }
    }

    private void validate(MovieSortRequest movieSortRequest) {
        Integer page = movieSortRequest.getPage();
        Integer count = movieSortRequest.getCount();

        if (page == null || page < 0) {
            throw new IllegalArgumentException("Page must be >= 0");
        }

        if (count == null || count <= 0) {
            throw new IllegalArgumentException("Count must be > 0");
        }
    }

    private List<MovieResponse> findAllWithoutPagination(MovieSortRequest movieSortRequest, Sort sort) {
        List<Movie> movies;

        if (movieSortRequest != null && movieSortRequest.getSearchText() != null) {
            Specification<Movie> spec = MovieSpecifications.containsTextInFields(movieSortRequest.getSearchText());
            movies = movieRepository.findAll(spec, sort);
        } else {
            movies = movieRepository.findAll(sort);
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
        movie.setPrice(correctMoviePrice);

        // For example, not all data is stored in a single database,
        // and it may not be possible to fetch everything within one transaction.
        enrichmentService.enrichMovie(movie);

        return movieMapper.toFullMovie(movie);
    }

    @Override
    @Transactional
    public void saveMovie(MovieRequest movieRequest) {
        Movie movie = movieMapper.toMovie(movieRequest);
        enrichmentService.enrichMovie(movie);

        movieRepository.save(movie);
        log.info("Successfully saved movie {} to the database", movie);
    }

    @Transactional
    @Override
    public FullMovieResponse updateMovie(Long id, MovieRequest movieRequest) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(String.format("Movie not found with ID: %d", id)));
        movieMapper.update(movie, movieRequest);
        enrichmentService.enrichMovie(movie);

        movieRepository.save(movie);

        log.info("Successfully updated movie id {} to the database", movie.getId());
        return movieMapper.toFullMovie(movie);

    }

    private Sort buildSort(MovieSortRequest movieSortRequest) {
        if (movieSortRequest == null) {
            return Sort.unsorted();
        }

        Sort.Direction ratingDir = convertRatingDirection(movieSortRequest.getRatingDirection());
        Sort.Direction priceDir = convertPriceDirection(movieSortRequest.getPriceDirection());

        if (ratingDir != null) {
            return Sort.by(ratingDir, RATING);
        } else if (priceDir != null) {
            return Sort.by(priceDir, PRICE);
        } else {
            return Sort.unsorted();
        }
    }

    private Sort.Direction convertRatingDirection(MovieSortRequest.RatingDirectionEnum ratingDirection) {
        return (ratingDirection == null) ? null : Sort.Direction.valueOf(ratingDirection.getValue());
    }

    private Sort.Direction convertPriceDirection(MovieSortRequest.PriceDirectionEnum priceDirection) {
        return (priceDirection == null) ? null : Sort.Direction.valueOf(priceDirection.getValue());
    }

    private boolean isPaginationRequested(MovieSortRequest movieSortRequest) {
        return movieSortRequest != null
                && movieSortRequest.getPage() != null
                && movieSortRequest.getCount() != null;
    }

    private List<MovieResponse> findAllWithPagination(MovieSortRequest movieSortRequest, Sort sort) {
        PageRequest pageable = PageRequest.of(
                Math.max(movieSortRequest.getPage(), 0),
                Math.max(movieSortRequest.getCount(), 1),
                sort
        );
        Specification<Movie> spec = MovieSpecifications.containsTextInFields(movieSortRequest.getSearchText());
        Page<Movie> moviePage = movieRepository.findAll(spec, pageable);
        return moviePage.map(movieMapper::toSimpleMovieResponse).getContent();
    }


}
