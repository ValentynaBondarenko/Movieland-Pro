package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.repository.GenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CacheTest {

    @Mock
    private GenreRepository genreRepository;

    private Cache<Genre> cache;


    @Test
    void refresh_shouldNotAddDuplicates() {
        // prepare
        Genre drama = new Genre(1L, "Drama");
        when(genreRepository.findAll()).thenReturn(List.of(drama));

        cache = new Cache<>(genreRepository::findAll);
        //when
        cache.refresh();

        when(genreRepository.findAll()).thenReturn(List.of(drama, drama));
        //when
        cache.refresh();

        Set<Genre> genres = cache.getAll();
        //then
        assertEquals(1, genres.size());
        assertTrue(genres.contains(drama));
    }

}