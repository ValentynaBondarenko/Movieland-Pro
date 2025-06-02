package com.bondarenko.movieland.service;

import com.bondarenko.listener.DataSourceListener;
import com.bondarenko.movieland.api.model.ResponseGenre;
import com.bondarenko.movieland.configuration.DataSourceProxyConfiguration;
import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.service.cache.GenreCacheAsideService;
import com.bondarenko.movieland.service.genre.GenreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Set;


import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @DisplayName("Should return genres from cache without additional DB queries")
    @Test
    void shouldReturnGenresFromCacheWithoutDBQueryAfterAppStart() {
        Set<ResponseGenre> genres = genreService.getAll();

        assertNotNull(genres);
        assertTrue(genres.stream().anyMatch(g -> g.getName().equals("Драма")));
        assertEquals(16, genres.size());

        DataSourceListener.assertSelectCount(0);

        Set<Genre> secondProbeOfGenres = genreCacheService.getGenre();
        assertNotNull(secondProbeOfGenres);
        assertTrue(genres.stream().anyMatch(g -> g.getName().equals("Драма")));
        assertEquals(16, secondProbeOfGenres.size());

        DataSourceListener.assertSelectCount(0);
    }
    @DisplayName("Should return only genres with matching IDs from cache")
    @Test
    void findByIdIn_shouldReturnGenresWithMatchingIds() {
        //prepare
        Set<Long> idsToFind = Set.of(1L, 3L, 5L);

        // when
        Set<Genre> result = genreService.findByIdIn(idsToFind);

        // then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(genre -> idsToFind.contains(genre.getId())));

        // should not hit DB
        DataSourceListener.assertSelectCount(0);
    }

}