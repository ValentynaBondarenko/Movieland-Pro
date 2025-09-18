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

import java.time.Instant;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParallelEnrichmentService implements EnrichmentService {
    private final CountryService countryService;
    private final GenreService genreService;
    private final ReviewService reviewService;

    @Value("${movieland.movie.enrichment.timeout}")
    private int timeout;

    @Override
    public void enrichMovie(MovieRequest movieRequest) {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            // запускаємо паралельні таски
            var genresFuture = scope.fork(() -> genreService.findByIdIn(
                    movieRequest.getGenres().stream().map(GenreResponse::getId).toList()
            ));

            var countriesFuture = scope.fork(() -> countryService.findByIdIn(
                    movieRequest.getCountries().stream().map(CountryResponse::getId).toList()
            ));

            var reviewsFuture = scope.fork(() -> reviewService.findByIdIn(
                    movieRequest.getReview().stream().map(ReviewResponse::getId).toList()
            ));

            // чекаємо завершення (з таймаутом!)
            scope.joinUntil(Instant.now().plusSeconds(timeout));
            scope.throwIfFailed(); // якщо хоч одна таска впала → кине виняток

            // якщо всі завершились успішно → зберігаємо результат
            movieRequest.setGenres(genresFuture.get());
            movieRequest.setCountries(countriesFuture.get());
            movieRequest.setReview(reviewsFuture.get());

        } catch (TimeoutException e) {
            log.warn("Enrichment timed out after {} seconds", timeout);
        } catch (Exception e) {
            log.error("Error while enriching movie", e);
            throw new RuntimeException(e);
        }
    }


}