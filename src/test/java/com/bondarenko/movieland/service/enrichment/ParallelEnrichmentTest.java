package com.bondarenko.movieland.service.enrichment;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.bondarenko.movieland.entity.Movie;
import com.bondarenko.movieland.service.enrichment.task.CountryTask;
import com.bondarenko.movieland.service.enrichment.task.GenreTask;
import com.bondarenko.movieland.service.enrichment.task.ReviewTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;

import java.lang.reflect.Field;

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
    private Appender<ILoggingEvent> mockAppender;
    private Logger logger;

    @BeforeEach
    void setupLogger() {
        logger = (Logger) LoggerFactory.getLogger(ParallelEnrichmentService.class);
        mockAppender = mock(Appender.class);
        logger.addAppender(mockAppender);
    }

    @Test
    void enrichMovie_shouldThrowTimeoutException_whenTasksAreTooLong() throws NoSuchFieldException, IllegalAccessException {

        ParallelEnrichmentService spyService = spy(service);

        Field timeoutField = ParallelEnrichmentService.class.getDeclaredField("timeout");
        timeoutField.setAccessible(true);
        timeoutField.setInt(spyService, 1);

        Movie movie = new Movie();
        movie.setId(1L);

        Runnable longTask = () -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }
        };

        doReturn(longTask).when(spyService).getGenresTask(any());
        doReturn(longTask).when(spyService).getCountriesTask(any());
        doReturn(longTask).when(spyService).getReviewsTask(any());

        //when
        spyService.enrichMovie(movie);

        //than
        verify(mockAppender, atLeastOnce()).doAppend(argThat(
                event -> event.getLevel().toString().equals("WARN")
                        && event.getFormattedMessage().contains("did not complete")
        ));
    }

    @Test
    void enrichMovie_shouldThrowTimeoutException_whenOneTaskIsTooLong() throws IllegalAccessException, NoSuchFieldException {
        ParallelEnrichmentService spyService = spy(service);

        Field timeoutField = ParallelEnrichmentService.class.getDeclaredField("timeout");
        timeoutField.setAccessible(true);
        timeoutField.setInt(spyService, 5);

        Movie movie = new Movie();
        movie.setId(1L);

        Runnable longTask = () -> {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException ignored) {
            }
        };
        Runnable fastTask = () -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        };

        doReturn(longTask).when(spyService).getGenresTask(any());
        doReturn(fastTask).when(spyService).getCountriesTask(any());
        doReturn(fastTask).when(spyService).getReviewsTask(any());

        //when
        spyService.enrichMovie(movie);

        //than
        verify(mockAppender, atLeastOnce()).doAppend(argThat(
                event -> event.getLevel().toString().equals("WARN")
                        && event.getFormattedMessage().contains("did not complete")
        ));
    }

    @Test
    void enrichMovie_shouldFinishSuccessfully_whenTasksCompleteWithinTimeout() throws NoSuchFieldException, IllegalAccessException {
        ParallelEnrichmentService spyService = spy(service);

        Field timeoutField = ParallelEnrichmentService.class.getDeclaredField("timeout");
        timeoutField.setAccessible(true);
        timeoutField.setInt(spyService, 5);

        Movie movie = new Movie();
        movie.setId(1L);

        Runnable fastTask = () -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        };

        doReturn(fastTask).when(spyService).getGenresTask(any());
        doReturn(fastTask).when(spyService).getCountriesTask(any());
        doReturn(fastTask).when(spyService).getReviewsTask(any());

        //when
        spyService.enrichMovie(movie);
        //then
        assertTrue(movie != null);
        verify(spyService, times(1)).getGenresTask(any());
        verify(spyService, times(1)).getCountriesTask(any());
        verify(spyService, times(1)).getReviewsTask(any());

        verify(mockAppender, never()).doAppend(argThat(
                event -> event.getLevel().toString().equals("WARN")
                        && event.getFormattedMessage().contains("did not complete")
        ));
    }

}


