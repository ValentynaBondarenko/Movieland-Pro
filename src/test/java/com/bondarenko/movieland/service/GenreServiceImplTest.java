package com.bondarenko.movieland.service;

import com.bondarenko.movieland.api.model.ResponseGenre;
import com.bondarenko.movieland.configuration.DataSourceProxyConfiguration;
import com.bondarenko.movieland.mapper.GenreMapper;
import com.bondarenko.movieland.repository.GenreRepository;
import com.bondarenko.movieland.service.cache.GenreCacheService;
import com.bondarenko.movieland.service.genre.GenreService;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.vladmihalcea.sql.SQLStatementCountValidator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static com.vladmihalcea.sql.SQLStatementCountValidator.assertSelectCount;

@DBRider
@SpringBootTest(classes = {DataSourceProxyConfiguration.class})
@Testcontainers
@TestPropertySource(properties = "GENRE_CACHE_UPDATE=60")
class GenreServiceImplTest extends AbstractITest {
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private GenreMapper genreMapper;

    @Autowired
    private GenreService genreService;

    @Autowired
    private GenreCacheService genreCacheService;


    @Test
    @ExpectedDataSet(value = "datasets/genre/datasets_genres.yml")
    public void testFindAllGenres() {

        List<ResponseGenre> genres = genreService.getAllGenres();

        assertNotNull(genres);
        assertEquals(16, genres.size());
    }

    @Test
    public void testFindAllGenresFromCache() {
        SQLStatementCountValidator.reset();
        genreService.getAllGenres();
        genreService.getAllGenres();
        genreService.getAllGenres();
        genreService.getAllGenres();
        assertSelectCount(1);

    }

    @Test
    public void testFindAllGenresFromCacheAfterRefreshTime() throws InterruptedException {
        SQLStatementCountValidator.reset();
        genreService.getAllGenres();
        Thread.sleep(600);
        genreService.getAllGenres();
        genreService.getAllGenres();
        genreService.getAllGenres();
        assertSelectCount(2);

    }
}