package com.bondarenko.movieland.controller;

import com.bondarenko.movieland.api.model.ResponseGenre;
import com.bondarenko.movieland.configuration.SecurityConfig;
import com.bondarenko.movieland.service.genre.GenreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GenreController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = true)
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnAllGenres() throws Exception {
        when(genreService.getAll()).thenReturn(getMockGenres());

        mockMvc.perform(get("/api/v1/genre"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Мелодрама"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Драма"));

        verify(genreService).getAll();
    }

    private List<ResponseGenre> getMockGenres() {
        ResponseGenre melodrama = new ResponseGenre();
        melodrama.setId(1);
        melodrama.setName("Мелодрама");

        ResponseGenre drama = new ResponseGenre();
        drama.setId(2);
        drama.setName("Драма");

        return List.of(melodrama, drama);
    }
}
