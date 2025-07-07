package com.bondarenko.movieland.service.genre;

import com.bondarenko.listener.DataSourceListener;
import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.service.AbstractITest;
import com.bondarenko.movieland.service.cache.GenreCacheProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
class GenreServiceImplTest extends AbstractITest {
    @Autowired
    private GenreService genreService;

    @Autowired
    private GenreCacheProxy genreCacheService;

    @BeforeEach
    void setUp() {
        DataSourceListener.reset();
    }

    @DisplayName("Should return genres from cache without additional DB queries")
    @Test
    void shouldReturnGenresFromCacheWithoutDBQueryAfterAppStart() {
        Set<GenreResponse> genres = genreService.findAll();

        assertNotNull(genres);
        assertTrue(genres.stream().anyMatch(g -> {
            assert g.getName() != null;
            return g.getName().equals("Драма");
        }));
        assertEquals(16, genres.size());

        DataSourceListener.assertSelectCount(0);

        Set<GenreResponse> secondProbeOfGenres = genreCacheService.findAll();
        assertNotNull(secondProbeOfGenres);
        assertTrue(genres.stream().anyMatch(g -> g.getName() != null && g.getName().equals("Драма")));
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

       DataSourceListener.assertSelectCount(0);
    }

}