package com.bondarenko.movieland.service;

import com.bondarenko.listener.*;
import com.bondarenko.movieland.api.model.*;
import com.bondarenko.movieland.configuration.*;
import com.bondarenko.movieland.service.cache.*;
import com.bondarenko.movieland.service.genre.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@TestPropertySource(properties = "GENRE_CACHE_UPDATE=1000")
@SpringBootTest(classes = {DataSourceProxyConfiguration.class})
class GenreServiceImplTest extends AbstractITest {
    @Autowired
    private GenreService genreService;

    @Autowired
    private GenreCacheAsideService genreCacheService;

    @BeforeEach
    void setUp() {
        DataSourceListener.reset();
    }

    @Test
    void testFindAllGenresFromCacheOnTheStartApp() {
        List<ResponseGenre> genres = genreService.getAll();

        assertNotNull(genres);
        assertEquals(16, genres.size());
        DataSourceListener.assertSelectCount(0);
    }

    @Test
    void testFindAllGenresFromCacheAfterRefreshTime() throws InterruptedException {
        List<ResponseGenre> allGenres = genreService.getAll();

        assertNotNull(allGenres);
        assertEquals(16, allGenres.size());

        Thread.sleep(genreCacheService.getCacheUpdateInterval()+200);
        List<ResponseGenre> genres = genreService.getAll();

        assertNotNull(genres);
        assertEquals(16, genres.size());

        DataSourceListener.assertSelectCount(1);

    }
}