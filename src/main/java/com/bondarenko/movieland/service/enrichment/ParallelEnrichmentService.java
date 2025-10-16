package com.bondarenko.movieland.service.enrichment;

import com.bondarenko.movieland.entity.Movie;
import com.bondarenko.movieland.exception.TimeoutEnrichMovieException;
import com.bondarenko.movieland.service.enrichment.task.CountryTask;
import com.bondarenko.movieland.service.enrichment.task.GenreTask;
import com.bondarenko.movieland.service.enrichment.task.ReviewTask;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParallelEnrichmentService implements EnrichmentService {
    private final ObjectProvider<GenreTask> genreTaskProvider;
    private final ObjectProvider<CountryTask> countryTaskProvider;
    private final ObjectProvider<ReviewTask> reviewTaskProvider;

    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    @Value("${movieland.movie.enrichment.timeout}")
    private int timeout;

    public void enrichMovie(Movie movie) {
        log.info("Starting enrichment for movie {}", movie.getId());

        CompletableFuture<Void> genreFuture = runTaskWithTimeout("GenreTask", getGenresTask(movie));
        CompletableFuture<Void> countryFuture = runTaskWithTimeout("CountryTask", getCountriesTask(movie));
        CompletableFuture<Void> reviewFuture = runTaskWithTimeout("ReviewTask", getReviewsTask(movie));

        CompletableFuture.allOf(genreFuture, countryFuture, reviewFuture)
                .whenComplete((r, ex) -> {
                    if (ex != null) log.error("Enrichment failed", ex);
                    else log.info("Enrichment finished successfully for movie {}", movie.getId());
                })
                .join();

    }

    private CompletableFuture<Void> runTaskWithTimeout(String taskName, Runnable task) {
        return CompletableFuture.runAsync(task, executor)
                .orTimeout(timeout, TimeUnit.SECONDS)
                .exceptionally(ex -> {
                    log.warn("{} did not complete in {} s and was skipped", taskName, timeout);
                    return null;
                });
    }

    protected Runnable getGenresTask(Movie movie) {
        // Use a fresh prototype instance to prevent race conditions between threads
        GenreTask task = genreTaskProvider.getObject();
        task.setMovie(movie);
        return task;
    }

    protected Runnable getCountriesTask(Movie movie) {
        CountryTask task = countryTaskProvider.getObject();
        task.setMovie(movie);
        return task;
    }

    protected Runnable getReviewsTask(Movie movie) {
        ReviewTask task = reviewTaskProvider.getObject();
        task.setMovie(movie);
        return task;
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