package com.bondarenko.movieland.controller;

import com.bondarenko.movieland.api.model.MovieRequest;
import com.bondarenko.movieland.configuration.SecurityConfig;
import com.bondarenko.movieland.service.movie.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;


import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MovieController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = true)
class MovieControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MovieService movieService;
    private MovieRequest movieRequest;

    @BeforeEach
    void setUp() {
        movieRequest = getMovieRequest();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAllAdminAddNewMovies() throws Exception {
        // when + then
        mockMvc.perform(post("/api/v1/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieRequest)))
                .andExpect(status().isCreated());

        verify(movieService).saveMovie(any(MovieRequest.class));

    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldForbidNotAdminFromAddingMovie() throws Exception {

        // when + then
        mockMvc.perform(post("/api/v1/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieRequest)))
                .andExpect(status().isForbidden());

        verify(movieService, never()).saveMovie(any(MovieRequest.class));

    }

    @Test
    void shouldRejectUnauthorizedUser() throws Exception {
        // when + then
        mockMvc.perform(post("/api/v1/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieRequest)))
                .andExpect(status().isUnauthorized());

        verify(movieService, never()).saveMovie(any(MovieRequest.class));
    }

    private MovieRequest getMovieRequest() {
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setNameUkrainian("Втеча з Шоушенка");
        movieRequest.setNameNative("The Shawshank Redemption");
        movieRequest.setYearOfRelease(1994);
        movieRequest.setDescription("Успішний банкір Енді Дюфрейн обвинувачений у вбивстві...");
        movieRequest.setPrice(123.45);
        movieRequest.setPicturePath("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg");
        movieRequest.setCountries(Arrays.asList(1, 2));
        movieRequest.setGenres(Arrays.asList(1, 2, 3));
        return movieRequest;
    }

}