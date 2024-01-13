package com.bondarenko.movieland.service;

import com.bondarenko.movieland.MovielandApplication;
import com.bondarenko.movieland.api.model.ResponseGenreDTO;
import com.bondarenko.movieland.configuration.DataSourceProxyConfiguration;
import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.mapper.GenreMapper;
import com.bondarenko.movieland.repository.GenreRepository;
import com.bondarenko.movieland.service.cache.GenreCacheService;
import com.bondarenko.movieland.service.genre.GenreService;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static com.vladmihalcea.sql.SQLStatementCountValidator.assertSelectCount;

@SpringBootTest(classes = { DataSourceProxyConfiguration.class})
@Testcontainers
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
    public void testFindAllGenres() {

        List<ResponseGenreDTO> genres = genreService.getAllGenres();

        Assertions.assertNotNull(genres);
        Assertions.assertEquals(16, genres.size());
        ResponseGenreDTO firstDTO = genres.get(0);
        ResponseGenreDTO genre = testDTO();
        Assertions.assertEquals(genre.getId(), firstDTO.getId());
        Assertions.assertEquals(genre.getId(), firstDTO.getId());
        Assertions.assertEquals(genre.getName(), firstDTO.getName());
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


    private ResponseGenreDTO testDTO() {
        ResponseGenreDTO responseGenreDTO = new ResponseGenreDTO();
        responseGenreDTO.setId(1);
        responseGenreDTO.setName("Драма");
        return responseGenreDTO;
    }

    private Genre createGenre(int id, String name) {
        Genre genre = new Genre();
        genre.setId(id);
        genre.setName(name);
        return genre;
    }
}