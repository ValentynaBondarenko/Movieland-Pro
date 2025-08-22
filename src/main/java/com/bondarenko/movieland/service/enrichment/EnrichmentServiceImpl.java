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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrichmentServiceImpl implements EnrichmentService {
    private final CountryService countryService;
    private final GenreService genreService;
    private final ReviewService reviewService;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public MovieRequest enrichMovie(MovieRequest movieRequest) {
        Callable<List<GenreResponse>> genresTask = () -> {
            List<Long> genreIds = movieRequest.getGenres().stream()
                    .map(GenreResponse::getId)
                    .toList();
            return genreService.findByIdIn(genreIds);
        };
        Callable<List<CountryResponse>> countiesTask = () -> {
            List<Long> countryIds = movieRequest.getCountries().stream()
                    .map(CountryResponse::getId)
                    .toList();

            return countryService.findByIdIn(countryIds);
        };
        Callable<List<ReviewResponse>> reviewsTask = () -> {
            List<Long> reviewIds = movieRequest.getReview().stream()
                    .map(ReviewResponse::getId)
                    .toList();
            return reviewService.findByIdIn(reviewIds);
        };

        //Run Thread pool:
        Future<List<GenreResponse>> genresFuture = executor.submit(genresTask);
        Future<List<CountryResponse>> countriesFuture = executor.submit(countiesTask);
        Future<List<ReviewResponse>> reviewsFuture = executor.submit(reviewsTask);

        try {
            movieRequest.setGenres(genresFuture.get(5, TimeUnit.SECONDS));
        } catch (TimeoutException e) {
            log.error("Genres enrichment timed out");
            genresFuture.cancel(true);
        } catch (InterruptedException e) {
            log.warn("Thread was interrupted while fetching genres");
            genresFuture.cancel(true);
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            log.error("Error while fetching genres", e.getCause());
        }

        try {
            movieRequest.setCountries(countriesFuture.get(5, TimeUnit.SECONDS));
        } catch (TimeoutException e) {
            log.error("Countries enrichment timed out");
            countriesFuture.cancel(true);
        } catch (InterruptedException e) {
            log.warn("Thread was interrupted while fetching countries");
            countriesFuture.cancel(true);
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            log.error("Error while fetching countries", e.getCause());
        }

        try {
            movieRequest.setReview(reviewsFuture.get(5, TimeUnit.SECONDS));
        } catch (TimeoutException e) {
            log.error("Reviews enrichment timed out");
            reviewsFuture.cancel(true);
        } catch (InterruptedException e) {
            log.warn("Thread was interrupted while fetching reviews");
            reviewsFuture.cancel(true);
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            log.error("Error while fetching reviews", e.getCause());
        }
        return movieRequest;
    }
}