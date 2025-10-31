package com.bondarenko.movieland.service.enrichment;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.bondarenko.movieland.entity.Movie;
import com.bondarenko.movieland.service.country.CountryService;
import com.bondarenko.movieland.service.genre.GenreService;
import com.bondarenko.movieland.service.review.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParallelEnrichmentTest {
    @Mock
    private GenreService genreService;
    @Mock
    private CountryService countryService;
    @Mock
    private ReviewService reviewService;

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
    void enrichMovie_shouldHandleInterruptedExceptionGracefully() throws Exception {
        // given
        ExecutorService mockExecutor = mock(ExecutorService.class);
        when(mockExecutor.invokeAll(anyList(), anyLong(), any()))
                .thenThrow(new InterruptedException());

        injectPrivateField(service, "executor", mockExecutor);
        injectPrivateField(service, "timeout", 1);

        Movie movie = new Movie();
        movie.setId(99L);

        // when
        service.enrichMovie(movie);

        // then
        verify(mockAppender, atLeastOnce()).doAppend(argThat(
                e -> e.getFormattedMessage().contains("Thread was interrupted")
        ));
        assertThat(Thread.currentThread().isInterrupted()).isTrue();
    }

    @Test
    void enrichMovie_shouldCompleteSuccessfullyWithinTimeout() throws Exception {
        // given
        ExecutorService spyExecutor = spy(Executors.newSingleThreadExecutor());
        injectPrivateField(service, "executor", spyExecutor);
        injectPrivateField(service, "timeout", 3);

        Movie movie = new Movie();
        movie.setId(42L);

        // when
        service.enrichMovie(movie);

        // then
        verify(spyExecutor, times(1))
                .invokeAll(anyList(), eq(3L), eq(TimeUnit.SECONDS));
        verify(mockAppender, atLeastOnce()).doAppend(argThat(
                e -> e.getFormattedMessage().contains("invokeAll starting")
        ));
    }

    @Test
    void shutdownExecutor_shouldShutdownExecutorService() throws Exception {
        ExecutorService mockExecutor = mock(ExecutorService.class);
        injectPrivateField(service, "executor", mockExecutor);

        // when
        service.shutdownExecutor();

        // then
        verify(mockExecutor).shutdown();
    }

    // --- helper method for reflection ---
    private void injectPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = ParallelEnrichmentService.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}


