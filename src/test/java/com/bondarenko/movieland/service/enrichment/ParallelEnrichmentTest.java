package com.bondarenko.movieland.service.enrichment;

import com.bondarenko.movieland.entity.Movie;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void enrichMovie_shouldThrowTimeoutException_whenTasksAreTooLong() {
        ParallelEnrichmentService spyService = spy(service);

        Runnable longTask = () -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {
            }
        };
        doReturn(longTask).when(spyService).getGenresTask(any());
        doReturn(longTask).when(spyService).getCountriesTask(any());
        doReturn(longTask).when(spyService).getReviewsTask(any());

        TimeoutEnrichMovieException ex = assertThrows(
                TimeoutEnrichMovieException.class,
                () -> spyService.enrichMovie(new Movie())
        );

        assertTrue(ex.getMessage().contains("cancelled by timeout"));
    }

}


