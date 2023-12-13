package com.bondarenko.movieland.controller;


import com.bondarenko.movieland.api.MovieApi;
import com.bondarenko.movieland.api.model.ResponseMovieDTO;
import com.bondarenko.movieland.service.movie.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/movie", produces = MediaType.APPLICATION_JSON_VALUE)
public class MovieController implements MovieApi {
    private MovieService movieService;

    @Override
    public ResponseEntity<List<ResponseMovieDTO>> findAllMovies(String rating, String price) {
        List<ResponseMovieDTO> response = (rating == null || price == null)
                ? movieService.findAllMovies()
                : movieService.findAllMoviesWithSorting(rating, price);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<List<ResponseMovieDTO>> getMoviesByGenre(Integer genreId) {
        log.info("Received request to find movies by genre with ID {}.", genreId);
        List<ResponseMovieDTO> movies = movieService.getMoviesByGenre(genreId);
        return ResponseEntity.ok(movies);
    }


    public ResponseEntity<List<ResponseMovieDTO>> getRandomMovies() {
        log.info("Received request to find random movies.");
        List<ResponseMovieDTO> response = movieService.getRandomMovies();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}