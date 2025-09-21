package com.bondarenko.movieland.service.enrichment;

import com.bondarenko.movieland.api.model.*;
import com.bondarenko.movieland.service.country.CountryService;
import com.bondarenko.movieland.service.genre.GenreService;
import com.bondarenko.movieland.service.review.ReviewService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParallelEnrichmentService implements EnrichmentService {
    private final CountryService countryService;
    private final GenreService genreService;
    private final ReviewService reviewService;

    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    @Value("${movieland.movie.enrichment.timeout}")
    private int timeout;

    public void enrichMovie(FullMovieResponse movieRequest) {
        List<Callable<Object>> parallelTasks = List.of(
                Executors.callable(getGenresTask(movieRequest)),
                Executors.callable(getCountriesTask(movieRequest)),
                Executors.callable(getReviewsTask(movieRequest))
        );

        try {
            executor.invokeAll(parallelTasks, timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.warn("Thread was interrupted while fetching");
            Thread.currentThread().interrupt();
        }
    }

    private <T> Runnable handleEnrichment(Supplier<T> supplier, Consumer<T> consumer, String taskName) {
        return () -> {
            long start = System.currentTimeMillis();
            log.info(">>> [{}] started in thread {} at {}", taskName,
                    System.identityHashCode(Thread.currentThread()), start);
            try {
                T result = supplier.get();
                consumer.accept(result);
                long end = System.currentTimeMillis();
                log.info("<<< [{}] finished in thread {} at {}. Duration: {} ms ({} s)",
                        taskName, System.identityHashCode(Thread.currentThread()), end,
                        (end - start), (end - start) / 1000.0);
            } catch (Exception e) {
                log.error("Error in task {}", taskName, e);
                throw new RuntimeException(e);
            }
        };
    }

    protected Runnable getGenresTask(FullMovieResponse movieRequest) {
        return handleEnrichment(
                () -> genreService.findByIdIn(
                        Optional.ofNullable(movieRequest.getGenres())
                                .orElse(List.of())
                                .stream()
                                .map(GenreResponse::getId)
                                .toList()
                ),
                movieRequest::setGenres,
                "genres"
        );
    }

    protected Runnable getCountriesTask(FullMovieResponse movieRequest) {
        return handleEnrichment(
                () -> countryService.findByIdIn(
                        Optional.ofNullable(movieRequest.getCountries())
                                .orElse(List.of())
                                .stream()
                                .map(CountryResponse::getId)
                                .toList()
                ),
                movieRequest::setCountries,
                "countries"
        );
    }

    protected Runnable getReviewsTask(FullMovieResponse movieRequest) {
        return handleEnrichment(
                () -> Optional.ofNullable(reviewService.findByMovieId(movieRequest.getId()))
                        .orElse(List.of()),
                movieRequest::setReviews,
                "reviews"
        );
    }


    //@PreDestroy will be triggered on:
// - Ctrl+C in the console, docker stop, kubectl delete pod, systemd stop,Kubernetes ->pod=SIGTERM
// --->Spring Boot run shutdown hook in JVM [Runtime.getRuntime().addShutdownHook(new Thread(context::close));]
//⚠️ It will NOT be invoked:
// - kill -9 (SIGKILL, no chance for cleanup)
//- fatal JVM crash (e.g. OutOfMemoryError, SIGSEGV)
    @PreDestroy
    public void shutdownExecutor() {
        executor.shutdown();
    }

}