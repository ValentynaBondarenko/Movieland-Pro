package com.bondarenko.movieland.service;

import com.bondarenko.listener.DataSourceListener;
import com.bondarenko.movieland.api.model.ResponseGenre;
import com.bondarenko.movieland.configuration.DataSourceProxyConfiguration;
import com.bondarenko.movieland.service.cache.GenreCacheAsideService;
import com.bondarenko.movieland.service.genre.GenreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@TestPropertySource(properties = "GENRE_CACHE_UPDATE=500")
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

//    @Test
//    void testFindAllGenresFromCacheOnTheStartApp() {
//        List<ResponseGenre> genres = genreService.getAll();
//
//        assertNotNull(genres);
//        assertEquals(16, genres.size());
//        DataSourceListener.assertSelectCount(0);
//    }


}