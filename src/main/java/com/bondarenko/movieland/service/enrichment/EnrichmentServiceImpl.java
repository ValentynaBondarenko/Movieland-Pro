package com.bondarenko.movieland.service.enrichment;

import com.bondarenko.movieland.api.model.CountryResponse;
import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.api.model.MovieRequest;
import com.bondarenko.movieland.api.model.ReviewResponse;
import com.bondarenko.movieland.service.country.CountryService;
import com.bondarenko.movieland.service.genre.GenreService;
import com.bondarenko.movieland.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrichmentServiceImpl implements EnrichmentService {
    private final CountryService countryService;
    private final GenreService genreService;
    private final ReviewService reviewService;

    @Value("${movieland.movie.enrichment.timeout}")
    private int timeout;


    public MovieRequest enrichMovie(MovieRequest movieRequest) {
        try (var scope = new StructuredTaskScope<>()) {
            var genresFuture = scope.fork(() -> getGenresTask(movieRequest).call());
            var countriesFuture = scope.fork(() -> getCountriesTask(movieRequest).call());
            var reviewsFuture = scope.fork(() -> getReviewsTask(movieRequest).call());

            scope.joinUntil(Instant.now().plus(Duration.ofSeconds(timeout)));

            movieRequest.setGenres(safeResult(genresFuture, "genres"));
            movieRequest.setCountries(safeResult(countriesFuture, "countries"));
            movieRequest.setReview(safeResult(reviewsFuture, "reviews"));

            return movieRequest;

        } catch (TimeoutException e) {
            log.warn("enrichMovie timed out", e);
            return movieRequest;
        } catch (InterruptedException e) {
            log.warn("enrichMovie was interrupted", e);
            Thread.currentThread().interrupt();
            return movieRequest;
        }
    }

    private static <T> List<T> safeResult(StructuredTaskScope.Subtask<List<T>> future, String taskName) {
        return switch (future.state()) {
            case SUCCESS -> future.get();
            case FAILED -> {
                log.error("Task {} failed", taskName, future.exception());
                yield List.of();
            }
            case UNAVAILABLE -> {
                log.warn("Task {} did not complete in time or was unavailable", taskName);
                yield List.of();
            }
        };
    }

    private Callable<List<ReviewResponse>> getReviewsTask(MovieRequest movieRequest) {
        return () -> reviewService.findByIdIn(
                movieRequest.getReview().stream().map(ReviewResponse::getId).toList()
        );
    }

    private Callable<List<CountryResponse>> getCountriesTask(MovieRequest movieRequest) {
        return () -> countryService.findByIdIn(
                movieRequest.getCountries().stream().map(CountryResponse::getId).toList()
        );
    }

    private Callable<List<GenreResponse>> getGenresTask(MovieRequest movieRequest) {
        return () -> genreService.findByIdIn(
                movieRequest.getGenres().stream().map(GenreResponse::getId).toList()
        );
    }
}