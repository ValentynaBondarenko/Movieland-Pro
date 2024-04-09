package com.bondarenko.movieland.service;

import com.bondarenko.movieland.api.model.ResponseGenre;
import com.bondarenko.movieland.configuration.DataSourceProxyConfiguration;
import com.bondarenko.movieland.service.cache.GenreCacheAsideService;
import com.bondarenko.movieland.service.genre.GenreService;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static com.vladmihalcea.sql.SQLStatementCountValidator.assertSelectCount;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@TestPropertySource(properties = "GENRE_CACHE_UPDATE=1000")
@SpringBootTest(classes = {DataSourceProxyConfiguration.class})
class GenreServiceImplTest extends AbstractITest {
    @Autowired
    private GenreService genreService;

    @Autowired
    private GenreCacheAsideService genreCacheService;

    @Test
    void testFindAllGenresFromCache() {
        SQLStatementCountValidator.reset();

        List<ResponseGenre> genres = genreService.getAll();

        assertNotNull(genres);
        assertEquals(16, genres.size());
        SQLStatementCountValidator.assertSelectCount(0);
    }

    @Test
    void testFindAllGenresFromCacheAfterRefreshTime() throws InterruptedException {
        SQLStatementCountValidator.reset();


        List<ResponseGenre> allGenres = genreService.getAll();

        assertNotNull(allGenres);
        assertEquals(16, allGenres.size());

       // Thread.sleep(genreCacheService.getCacheUpdateInterval()+200);
        List<ResponseGenre> genres = genreService.getAll();

        assertNotNull(genres);
        assertEquals(16, genres.size());

        assertSelectCount(1);

    }
}