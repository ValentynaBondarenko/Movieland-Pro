package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.repository.GenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CacheTest {

    @Mock
    private GenreRepository genreRepository;

    @Test
    void refresh_shouldReplaceCacheWithLatestData() {
        // prepare
        Genre drama = new Genre(1L, "Drama");
        Genre action = new Genre(2L, "Action");

        // first-invoke cache
        when(genreRepository.findAll()).thenReturn(List.of(drama));
        Cache<Genre> cache = new Cache<>(genreRepository::findAll);
        cache.refresh();

        List<Genre> firstLoad = cache.getAll();
        assertEquals(1, firstLoad.size());
        assertTrue(firstLoad.contains(drama));

        // second-invoke cache
        when(genreRepository.findAll()).thenReturn(List.of(action));
        cache.refresh();

        List<Genre> secondLoad = cache.getAll();
        assertEquals(1, secondLoad.size());
        assertTrue(secondLoad.contains(action));
        assertFalse(secondLoad.contains(drama));
    }

}