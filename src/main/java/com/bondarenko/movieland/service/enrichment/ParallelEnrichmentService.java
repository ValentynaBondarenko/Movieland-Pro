package com.bondarenko.movieland.service.enrichment;

import com.bondarenko.movieland.entity.Country;
import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.entity.Movie;
import com.bondarenko.movieland.entity.Review;
import com.bondarenko.movieland.service.country.CountryService;
import com.bondarenko.movieland.service.genre.GenreService;
import com.bondarenko.movieland.service.review.ReviewService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParallelEnrichmentService implements EnrichmentService {
    private final GenreService genreService;
    private final CountryService countryService;
    private final ReviewService reviewService;

    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    @Value("${movieland.movie.enrichment.timeout}")
    private int timeout;


    public void enrichMovie(Movie movie) {
        log.info("Starting enrichment for movie {}", movie.getId());
        List<Callable<Object>> parallelTasks = List.of(
                Executors.callable(getGenresTask(movie)),
                Executors.callable(getCountriesTask(movie)),
                Executors.callable(getReviewsTask(movie))
        );
        try {
            log.info(">>> invokeAll starting...");
            List<Future<Object>> futures = executor.invokeAll(parallelTasks, timeout, TimeUnit.SECONDS);
            log.info(">>> invokeAll returned {} futures", futures.size());
        } catch (InterruptedException e) {
            log.warn("Thread was interrupted while fetching");
            Thread.currentThread().interrupt();
        }
    }

    private Runnable getCountriesTask(Movie movie) {
        return () -> {
            List<Long> countryIds = movie.getCountries().stream()
                    .map(Country::getId)
                    .toList();

            List<Country> countries = new ArrayList<>(countryService.findById(countryIds));
            movie.setCountries(countries);
        };
    }

    private Runnable getGenresTask(Movie movie) {
        return () -> {
            List<Long> genresIds = Optional.of(movie.getGenres())
                    .orElse(List.of())
                    .stream()
                    .map(Genre::getId)
                    .toList();

            List<Genre> genres = new ArrayList<>(genreService.findById(genresIds));
            movie.setGenres(genres);
        };
    }

    private Runnable getReviewsTask(Movie movie) {
        return () -> {
            List<Long> reviewIds = Optional.of(movie.getReviews())
                    .orElse(List.of())
                    .stream()
                    .map(Review::getId)
                    .toList();

            List<Review> reviews = new ArrayList<>(reviewService.findById(reviewIds));
            movie.setReviews(reviews);
        };
    }


    //@PreDestroy will be triggered on:
// - Ctrl+C in the console, docker stop, kubectl delete pod, systemd stop,Kubernetes ->pod=SIGTERM
// --->Spring Boot run shutdown hook in JVM [Runtime.getRuntime().addShutdownHook(new Thread(context::close));]
//⚠️ It will NOT be invoked:
// - kill -9 (SIGKILL, no chance for cleanup)
//- fatal JVM crash (e.g. OutOfMemoryError, SIGSEGV)
//⚠️️ Note:
// - Prototype beans are not managed by the container after creation, so their destroy methods are not called automatically.
    @PreDestroy
    public void shutdownExecutor() {
        executor.shutdown();
    }

}