package com.bondarenko.movieland.service;

import com.bondarenko.movieland.MovielandApplication;
import com.bondarenko.movieland.dto.ResponseGenreDTO;
import com.bondarenko.movieland.mapper.GenreMapper;
import com.bondarenko.movieland.repository.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(classes = MovielandApplication.class)
@Testcontainers
class GenreServiceImplTest extends AbstractITest {
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private GenreMapper genreMapper;

    @Autowired
    private GenreService genreService;

    @Test
    public void testFindAllGenres() {

        List<ResponseGenreDTO> genres = genreService.getAllGenres();

        assertNotNull(genres);
        assertEquals(16, genres.size());
        ResponseGenreDTO firstDTO = genres.get(0);
        ResponseGenreDTO genre = testDTO();
        assertEquals(genre.getId(), firstDTO.getId());
        assertEquals(genre.getId(), firstDTO.getId());
        assertEquals(genre.getName(), firstDTO.getName());
    }

    private ResponseGenreDTO testDTO() {
        return ResponseGenreDTO.builder()
                .id(1)
                .name("Драма")
                .build();
    }
}