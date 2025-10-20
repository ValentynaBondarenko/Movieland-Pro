package com.bondarenko.movieland.service.cache;

import com.bondarenko.listener.DataSourceListener;
import com.bondarenko.movieland.api.model.CountryResponse;
import com.bondarenko.movieland.api.model.FullMovieResponse;
import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.api.model.ReviewResponse;
import com.bondarenko.movieland.service.AbstractITest;
import com.bondarenko.movieland.service.movie.MovieService;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DBRider
class MovieCacheProxyGcITest extends AbstractITest {
    @Autowired
    private MovieService movieService;
    @Autowired
    private MovieCacheProxy proxy;

    @BeforeEach
    void setUp() {
        DataSourceListener.reset();
        proxy.clearCacheForTests();
    }

    @Test
    @Tag("gc-test")
    @DataSet(value = "datasets/movie/dataset_movies.yml")
    void testGetMovieById_firstCall_missCache_thenHitCache() {

        FullMovieResponse movie = proxy.getMovieById(1L, null);

        assertEquals(1L, movie.getId());
        assertEquals("Втеча з Шоушенка", movie.getNameUkrainian());
        assertEquals("The Shawshank Redemption", movie.getNameNative());
        assertEquals(1994, movie.getYearOfRelease());
        assertEquals("Успішний банкір Енді Дюфрейн обвинувачений у вбивстві...", movie.getDescription());
        assertEquals(8.9, movie.getRating(), 0.001);
        assertEquals(123.45, movie.getPrice(), 0.001);
        assertEquals(
                "https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg",
                movie.getPicturePath()
        );

        // genres
        assertNotNull(movie.getGenres());
        assertEquals(1, movie.getGenres().size());
        GenreResponse genre = movie.getGenres().getFirst();
        assertEquals(1L, genre.getId());
        assertEquals("драма", genre.getName());

        // countries
        assertNotNull(movie.getCountries());
        assertEquals(1, movie.getCountries().size());
        CountryResponse country = movie.getCountries().getFirst();
        assertEquals(1L, country.getId());
        assertEquals("США", country.getName());

        // reviews
        assertNotNull(movie.getReviews());
        assertEquals(2, movie.getReviews().size());

        ReviewResponse review1 = movie.getReviews().getFirst();
        assertEquals(1L, review1.getId());
        assert review1.getUser() != null;
        assertEquals(1L, review1.getUser().getId());
        assertEquals("Дарлін Едвардс", review1.getUser().getNickname());
        assert review1.getText() != null;
        assertTrue(review1.getText().startsWith("Геніальний фільм! Дивишся"));

        ReviewResponse review2 = movie.getReviews().get(1);
        assertEquals(2L, review2.getId());
        assert review2.getUser() != null;
        assertEquals(2L, review2.getUser().getId());
        assertEquals("Габріель Джексон", review2.getUser().getNickname());
        assert review2.getText() != null;
        assertTrue(review2.getText().startsWith("Кіно це, безумовно"));
        DataSourceListener.assertSelectCount(4);

        //second time get movie by id. Must work cache
        DataSourceListener.reset();
        FullMovieResponse movieByIdSecond = proxy.getMovieById(1L, null);

        DataSourceListener.assertSelectCount(0);
        assertEquals(1L, movieByIdSecond.getId());
        assertEquals("Втеча з Шоушенка", movieByIdSecond.getNameUkrainian());
        assertEquals("The Shawshank Redemption", movieByIdSecond.getNameNative());
    }

    @Test
    @Tag("gc-test")
    @DataSet(value = "datasets/movie/dataset_movies.yml")
    void shouldEvictSoftReferencesUnderMemoryPressure() {
        heapSize();
        //prepare
        for (long i = 1; i < 25; i++) {
            proxy.getMovieById(i, null);
        }
        assertEquals(24, proxy.liveMovieReferences(), "Movies loaded into cache");
        //when
        fillMemoryToTrigerGC();

        int liveAfter = proxy.liveMovieReferences();
        assertTrue(liveAfter < 24, "Some refs should be cleared");
    }

    private void heapSize() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();

        log.info("Max heap: {} MB", maxMemory / 1024 / 1024);//Max heap: 64 MB
        log.info("Total heap: {} MB", totalMemory / 1024 / 1024);// Total heap: 64 MB
        log.info("Free heap: {} MB", freeMemory / 1024 / 1024);//Free heap: 7 MB
    }

    private void fillMemoryToTrigerGC() {
        List<byte[]> memory = new ArrayList<>();
        try {
            for (int i = 0; i < 58; i++) {  // 58MB
                memory.add(new byte[1024 * 1024]);
            }
        } catch (OutOfMemoryError e) {
            log.info("OOM caught - SoftReferences should be cleared");
        }
        System.gc();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}