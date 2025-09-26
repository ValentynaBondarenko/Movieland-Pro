package com.bondarenko.movieland.service.enrichment;

import com.bondarenko.movieland.api.model.MovieDto;
import com.bondarenko.movieland.service.enrichment.task.CountryTask;
import com.bondarenko.movieland.service.enrichment.task.GenreTask;
import com.bondarenko.movieland.service.enrichment.task.ReviewTask;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public void enrichMovie(MovieDto movieDto) {
        System.out.println("Enriching movie " + movieDto);
        List<Callable<Object>> parallelTasks = List.of(
                Executors.callable(getGenresTask(movieDto)),
                Executors.callable(getCountriesTask(movieDto)),
                Executors.callable(getReviewsTask(movieDto))
        );
        try {
            log.info(">>> invokeAll starting...");
            List<Future<Object>> futures = executor.invokeAll(parallelTasks, timeout, TimeUnit.SECONDS);
            log.info(">>> invokeAll returned {} futures", futures.size());
            cancelUnfinishedTasks(futures);
            System.out.println("Enriching movie 2 " + movieDto);

        } catch (InterruptedException e) {
            log.warn("Thread was interrupted while fetching");
            Thread.currentThread().interrupt();
        } catch (TimeoutException e) {
            log.error("Timeout occurred during movie enrichment", e);

        }
    }

    private void cancelUnfinishedTasks(List<Future<Object>> futures) throws TimeoutException {
        List<Integer> unfinishedIndexes = new ArrayList<>();
        for (int i = 0; i < futures.size(); i++) {
            Future<Object> future = futures.get(i);
            if (!future.isDone()) {
                future.cancel(true);
                log.warn("Task #{} did not complete within {} seconds and was cancelled", i, timeout);
                unfinishedIndexes.add(i);
            }
        }
        if (!unfinishedIndexes.isEmpty()) {
            throw new TimeoutException("Tasks not completed within " + timeout + " seconds: " + unfinishedIndexes);
        }
    }

    protected Runnable getGenresTask(MovieDto movieDto) {
        GenreTask task = genreTaskProvider.getObject();
        task.setMovieDto(movieDto);
        return task;
    }

    protected Runnable getCountriesTask(MovieDto movieDto) {
        CountryTask task = countryTaskProvider.getObject();
        task.setMovieDto(movieDto);
        return task;
    }

    protected Runnable getReviewsTask(MovieDto movieDto) {
        ReviewTask task = reviewTaskProvider.getObject();
        task.setMovieDto(movieDto);
        return task;
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
//private <T> Runnable logWrapper() {
//    return () -> {
//        long start = System.currentTimeMillis();
//        log.info(">>> [{}] started in thread {} at {}", taskName,
//                System.identityHashCode(Thread.currentThread()), start);
//        try {
//            T result = supplier.get();
//            consumer.accept(result);
//            long end = System.currentTimeMillis();
//            log.info("<<< [{}] finished in thread {} at {}. Duration: {} ms ({} s)",
//                    taskName, System.identityHashCode(Thread.currentThread()), end,
//                    (end - start), (end - start) / 1000.0);
//        } catch (Exception e) {
//            log.error("Error in task {}", taskName, e);
//            throw new RuntimeException(e);
//        }
//    };
//}
