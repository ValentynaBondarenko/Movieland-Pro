package com.bondarenko.movieland.service.enrichment;

import com.bondarenko.movieland.api.model.MovieDto;
import com.bondarenko.movieland.exception.TimeoutEnrichMovieException;
import com.bondarenko.movieland.service.enrichment.task.CountryTask;
import com.bondarenko.movieland.service.enrichment.task.GenreTask;
import com.bondarenko.movieland.service.enrichment.task.ReviewTask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ParallelEnrichmentTest {
    @Mock
    private ObjectProvider<GenreTask> genreTaskProvider;
    @Mock
    private ObjectProvider<CountryTask> countryTaskProvider;
    @Mock
    private ObjectProvider<ReviewTask> reviewTaskProvider;

    @Mock
    private GenreTask genreTask;
    @Mock
    private CountryTask countryTask;
    @Mock
    private ReviewTask reviewTask;

    @InjectMocks
    private ParallelEnrichmentService service;

    @Test
    void enrichMovie_shouldTimeoutWithLongTasks() {
        MovieDto dto = new MovieDto();

        ParallelEnrichmentService spyService = spy(service);

        doReturn((Runnable) () -> {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException ignored) {
            }
        }).when(spyService).getGenresTask(any());
        doReturn((Runnable) () -> {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException ignored) {
            }
        }).when(spyService).getCountriesTask(any());
        doReturn((Runnable) () -> {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException ignored) {
            }
        }).when(spyService).getReviewsTask(any());

        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> spyService.enrichMovie(dto));
    }

    @Test
    void enrichMovie_shouldThrowTimeoutWithoutReflection() {
        MovieDto dto = new MovieDto();

        ParallelEnrichmentService spyService = spy(service);

        doReturn((Runnable) () -> {
            try { Thread.sleep(7000); } catch (InterruptedException ignored) {}
        }).when(spyService).getGenresTask(any());
        doReturn((Runnable) () -> {
            try { Thread.sleep(7000); } catch (InterruptedException ignored) {}
        }).when(spyService).getCountriesTask(any());
        doReturn((Runnable) () -> {
            try { Thread.sleep(7000); } catch (InterruptedException ignored) {}
        }).when(spyService).getReviewsTask(any());

        TimeoutEnrichMovieException ex = assertThrows(
                TimeoutEnrichMovieException.class,
                () -> spyService.enrichMovie(dto)
        );

        assertTrue(ex.getMessage().contains("Tasks not completed within"));
    }

}


