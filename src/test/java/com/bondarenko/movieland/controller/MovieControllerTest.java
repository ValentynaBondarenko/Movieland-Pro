package com.bondarenko.movieland.controller;

import com.bondarenko.movieland.api.model.*;
import com.bondarenko.movieland.configuration.SecurityConfig;
import com.bondarenko.movieland.service.movie.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    void shouldAllAdminCanAddNewMovie() throws Exception {
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

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAllAdminUpdateMovieById() throws Exception {
        // when + then
        mockMvc.perform(put("/api/v1/movie/{id}", 100)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieRequest)))
                .andExpect(status().isOk());

        verify(movieService).updateMovie(any(Long.class), any(MovieRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldRejectNotAdminRoleWhenUpdateMovieById() throws Exception {
        // when + then
        mockMvc.perform(put("/api/v1/movie/{id}", 100)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieRequest)))
                .andExpect(status().isForbidden());

        verify(movieService, never()).updateMovie(any(Long.class), any(MovieRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnAllMovies() throws Exception {
        List<ResponseMovie> mockResponse = List.of(new ResponseMovie());
        when(movieService.findAll(any())).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/movie"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(movieService).findAll(any(MovieSortCriteria.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnMovieByIdWithCurrency() throws Exception {
        Long movieId = 1L;
        String currency = "USD";

        ResponseFullMovie responseMovie = new ResponseFullMovie();
        when(movieService.getMovieById(movieId, currency)).thenReturn(responseMovie);

        mockMvc.perform(get("/api/v1/movie/{id}", movieId)
                        .param("currency", currency))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(movieService).getMovieById(movieId, currency);
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnMoviesByGenre() throws Exception {
        Long genreId = 5L;
        List<ResponseMovie> mockResponse = List.of(new ResponseMovie());
        when(movieService.getMoviesByGenre(genreId)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/movie/genre/{id}", genreId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(movieService).getMoviesByGenre(genreId);
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnRandomMovies() throws Exception {
        List<ResponseMovie> mockResponse = List.of(new ResponseMovie());
        when(movieService.getRandomMovies()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/movie/random"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(movieService).getRandomMovies();
    }

    private MovieRequest getMovieRequest() {
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setNameUkrainian("Втеча з Шоушенка");
        movieRequest.setNameNative("The Shawshank Redemption");
        movieRequest.setYearOfRelease(1994);
        movieRequest.setDescription("Успішний банкір Енді Дюфрейн обвинувачений у вбивстві...");
        movieRequest.setPrice(123.45);
        movieRequest.setPicturePath("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg");

        Set<CountryDTO> countries = new HashSet<>();
        CountryDTO firstCountry = new CountryDTO();
        firstCountry.setId(1L);
        firstCountry.setName("США");
        countries.add(firstCountry);

        CountryDTO secondCountry = new CountryDTO();
        secondCountry.setId(2L);
        secondCountry.setName("Франція");
        countries.add(secondCountry);

        movieRequest.setCountries(countries);

        Set<GenreDTO> genres = new HashSet<>();

        GenreDTO firstGenre = new GenreDTO();
        firstGenre.setId(1L);
        firstGenre.setName("Драма");
        genres.add(firstGenre);

        GenreDTO secondGenre = new GenreDTO();
        secondGenre.setId(2L);
        secondGenre.setName("Кримінал");
        genres.add(secondGenre);

        GenreDTO thirdGenre = new GenreDTO();
        thirdGenre.setId(3L);
        thirdGenre.setName("Фентезі");
        genres.add(thirdGenre);

        movieRequest.setGenres(genres);
        return movieRequest;
    }

}