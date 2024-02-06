package com.bondarenko.movieland.service;

import com.bondarenko.movieland.api.model.ResponseGenre;
import com.bondarenko.movieland.configuration.DataSourceProxyConfiguration;
import com.bondarenko.movieland.mapper.GenreMapper;
import com.bondarenko.movieland.repository.GenreRepository;
import com.bondarenko.movieland.service.cache.GenreCacheService;
import com.bondarenko.movieland.service.genre.GenreService;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static com.vladmihalcea.sql.SQLStatementCountValidator.assertSelectCount;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(classes = {DataSourceProxyConfiguration.class})
@TestPropertySource(properties = "GENRE_CACHE_UPDATE=60")
class GenreServiceImplTest extends AbstractITest {

    @Autowired
    private GenreService genreService;

    @Autowired
    private GenreCacheService genreCacheService;

    @Test

    public void testFindAllGenres() {
        List<ResponseGenre> genres = genreService.getAllGenres();

        assertNotNull(genres);
        assertEquals(16, genres.size());
    }

    @Test
    public void testFindAllGenresFromCache() {
        SQLStatementCountValidator.reset();
        List<ResponseGenre> genres = genreService.getAllGenres();
        genreService.getAllGenres();
        assertSelectCount(1);

        assertNotNull(genres);
        assertEquals(16, genres.size());
    }

    @Test
    public void testFindAllGenresFromCacheAfterRefreshTime() throws InterruptedException {
        SQLStatementCountValidator.reset();
        genreService.getAllGenres();
        Thread.sleep(100);

        genreService.getAllGenres();
        List<ResponseGenre> genres = genreService.getAllGenres();

        assertNotNull(genres);
        assertEquals(16, genres.size());
        assertSelectCount(2);

    }
}