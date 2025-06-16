package com.bondarenko.movieland.controller;

import com.bondarenko.movieland.api.MoviesApi;
import com.bondarenko.movieland.api.model.FullMovieResponse;
import com.bondarenko.movieland.api.model.MovieRequest;
import com.bondarenko.movieland.api.model.MovieResponse;
import com.bondarenko.movieland.api.model.MovieSortRequest;
import com.bondarenko.movieland.service.movie.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1")
public class MovieController implements MoviesApi {
    private final MovieService movieService;

    @PostMapping(value = "/movies", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<Void> addMovie(@Valid @RequestBody MovieRequest movieRequest) {
        log.info("Received request to add new movie {}.", movieRequest);
        movieService.saveMovie(movieRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(
            value = "/movies/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<Void> editMovie(
            @PathVariable("id") Long id, @Valid @RequestBody MovieRequest movieRequest) {
        log.info("Received request to edit by id={} movie name {} .", id, movieRequest.getNameNative());
        movieService.updateMovie(id, movieRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/movies", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<List<MovieResponse>> findAllMovies(
            @Valid @ModelAttribute MovieSortRequest movieSortRequest) {
        log.info("Received request to find all movies ");
        List<MovieResponse> response = movieService.findAll(movieSortRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(
            value = "/movies/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Override
    public ResponseEntity<FullMovieResponse> getMovieById(
            @PathVariable("id") Long movieId,
            @RequestParam(name = "currency", required = false) String currency) {

        log.info("Received request to get movie by id: {}. And currency: {} .", movieId, currency);
        FullMovieResponse fullMovie = movieService.getMovieById(movieId, currency);
        return ResponseEntity.ok(fullMovie);
    }

    @GetMapping(
            value = "/movies/genres/{genreId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<List<MovieResponse>> getMoviesByGenre(
            @PathVariable("genreId") Long genreId) {
        log.info("Received request to find movies by genre with id: {}.", genreId);
        List<MovieResponse> movies = movieService.getMoviesByGenre(genreId);
        return ResponseEntity.ok(movies);
    }

    @GetMapping(
            value = "/movies/random",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<List<MovieResponse>> getRandomMovies() {
        log.info("Received request to find random movies.");
        List<MovieResponse> response = movieService.getRandomMovies();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}