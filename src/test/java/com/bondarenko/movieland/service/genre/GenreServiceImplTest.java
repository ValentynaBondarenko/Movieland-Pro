package com.bondarenko.movieland.service.genre;

import com.bondarenko.listener.DataSourceListener;
import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.service.AbstractITest;
import com.bondarenko.movieland.service.cache.GenreCacheProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
        List<GenreResponse> genresFromCache = genreCacheService.findAll();

        assertNotNull(genresFromCache);
        assertTrue(genresFromCache.stream().anyMatch(g -> "драма".equals(g.getName())));
        assertEquals(16, genresFromCache.size());

        DataSourceListener.assertSelectCount(0);
    }

    @DisplayName("Should return only genres with matching IDs from cache")
    @Test
    void findByIdIn_shouldReturnGenresWithMatchingIds() {
        //prepare
        List<Long> idsToFind = List.of(1L, 3L, 5L);

        // when
        List<GenreResponse> result = genreService.findByIdIn(idsToFind);

        // then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(genre -> idsToFind.contains(genre.getId())));

        DataSourceListener.assertSelectCount(0);
    }

}