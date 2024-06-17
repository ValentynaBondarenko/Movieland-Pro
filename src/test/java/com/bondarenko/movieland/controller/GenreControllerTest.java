package com.bondarenko.movieland.controller;

import com.bondarenko.movieland.api.model.*;
import com.bondarenko.movieland.service.genre.*;
import com.bondarenko.movieland.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.test.context.junit.jupiter.*;
import org.springframework.test.web.servlet.*;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GenreController.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @Test
    void testFindAllGenres() throws Exception {
        List<ResponseGenre> genres = TestGenres.getGenres();
        when(genreService.getAll()).thenReturn(genres);

        mockMvc.perform(get("/api/v1/genre"))
                .andExpect(status().isOk());
    }
}