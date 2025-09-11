package com.bondarenko.movieland.service.enrichment;

import com.bondarenko.movieland.api.model.CountryResponse;
import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.api.model.MovieRequest;
import com.bondarenko.movieland.api.model.ReviewResponse;
import com.bondarenko.movieland.service.country.CountryService;
import com.bondarenko.movieland.service.genre.GenreService;
import com.bondarenko.movieland.service.review.ReviewService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

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

    public MovieRequest enrichMovie(MovieRequest movieRequest) {
        Callable<List<GenreResponse>> genresTask = getGenresTask(movieRequest);
        Callable<List<CountryResponse>> countriesTask = getCountriesTask(movieRequest);
        Callable<List<ReviewResponse>> reviewsTask = getReviewsTask(movieRequest);

        List<GenreResponse> genres = fetchWithTimeout(genresTask, "genres");
        List<CountryResponse> countries = fetchWithTimeout(countriesTask, "countries");
        List<ReviewResponse> reviews = fetchWithTimeout(reviewsTask, "reviews");

        movieRequest.setGenres(genres);
        movieRequest.setCountries(countries);
        movieRequest.setReview(reviews);

        return movieRequest;
    }

    protected Callable<List<ReviewResponse>> getReviewsTask(MovieRequest movieRequest) {
        return () -> reviewService.findByIdIn(
                movieRequest.getReview().stream().map(ReviewResponse::getId).toList()
        );
    }

    protected Callable<List<CountryResponse>> getCountriesTask(MovieRequest movieRequest) {
        return () -> countryService.findByIdIn(
                movieRequest.getCountries().stream().map(CountryResponse::getId).toList()
        );
    }

    protected Callable<List<GenreResponse>> getGenresTask(MovieRequest movieRequest) {
        return () -> genreService.findByIdIn(
                movieRequest.getGenres().stream().map(GenreResponse::getId).toList()
        );
    }

    private <T> List<T> fetchWithTimeout(Callable<List<T>> task, String taskName) {
        Future<List<T>> future = executor.submit(() -> {
            long start = System.currentTimeMillis();
            log.info("---->>>  [{}] started in thread {} at {}.", taskName, System.identityHashCode(Thread.currentThread()), start);

            var result = task.call();

            long end = System.currentTimeMillis();
            log.info("<<<---- [{}] finished in thread {} at {}.", taskName, System.identityHashCode(Thread.currentThread()), end);
            log.info("Total duration: {} ms ({} s)", (end - start), (end - start) / 1000.0);

            return result;
        });

        try {
            return future.get(timeout, TimeUnit.SECONDS);
        } catch (TimeoutException _) {
            log.error("{} enrichment timed out", taskName);
            future.cancel(true);
        } catch (InterruptedException _) {
            log.warn("Thread was interrupted while fetching {}", taskName);
            future.cancel(true);
            Thread.currentThread().interrupt();//не вбиває потік, а лише встановлює йому «флаг переривання».
            //чекає у wait()/join()/sleep() → він викине InterruptedException, а флаг переривання обнулиться.
        } catch (ExecutionException e) {
            log.error("Error while fetching {}", taskName, e.getCause());
        }
        return List.of();
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