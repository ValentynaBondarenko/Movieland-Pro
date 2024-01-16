package com.bondarenko.movieland.controller;

import com.bondarenko.movieland.api.model.ResponseGenre;
import com.bondarenko.movieland.service.genre.GenreService;
import com.bondarenko.movieland.util.TestGenres;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GenreController.class)
class GenreControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private GenreController genreController;

    @Mock
    private GenreService genreService;

    @Test
    public void testFindAllGenres() throws Exception {
        List<ResponseGenre> genres = TestGenres.getGenres();
        when(genreService.getAllGenres()).thenReturn(genres);

        mockMvc = MockMvcBuilders.standaloneSetup(genreController).build();

        mockMvc.perform(get("/api/v1/genre"))
                .andExpect(status().isOk());
    }

}

