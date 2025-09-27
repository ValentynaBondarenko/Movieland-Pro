package com.bondarenko.movieland.web.controller;

import com.bondarenko.movieland.api.MoviesApi;
import com.bondarenko.movieland.api.model.FullMovieResponse;
import com.bondarenko.movieland.api.model.MovieDto;
import com.bondarenko.movieland.api.model.MovieResponse;
import com.bondarenko.movieland.api.model.MovieSortRequest;
import com.bondarenko.movieland.entity.CurrencyType;
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
@RequestMapping(path = "/api/v1/movies", produces = MediaType.APPLICATION_JSON_VALUE)
public class MovieController implements MoviesApi {
    private final MovieService movieService;

    @PostMapping
    @Override
    public ResponseEntity<Void> addMovie(@Valid @RequestBody MovieDto movieRequest) {
        log.info("Received request to add new movie {}.", movieRequest);
        movieService.saveMovie(movieRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{movieId}")
    @Override
    public ResponseEntity<Void> editMovie(@PathVariable("movieId") Long movieId, @Valid @RequestBody MovieDto movieRequest) {
        log.info("Received request to edit by id={} movie name {} .", movieId, movieRequest.getNameNative());
        movieService.updateMovie(movieId, movieRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<MovieResponse>> findAllMovies(@Valid @ModelAttribute MovieSortRequest movieSortRequest) {
        log.info("Received request to find all movies ");
        List<MovieResponse> response = movieService.findAll(movieSortRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{movieId}")
    @Override
    public ResponseEntity<FullMovieResponse> getMovieById(@PathVariable("movieId") Long movieId,
                                                          @RequestParam(name = "currency", required = false) String currency) {
        log.info("Received request to get movie by id: {}. And currency: {} .", movieId, currency);
        CurrencyType currencyType = CurrencyType.from(currency);

        FullMovieResponse fullMovie = movieService.getMovieById(movieId, currencyType);
        return ResponseEntity.ok(fullMovie);
    }

    @GetMapping("/genres/{genreId}")
    @Override
    public ResponseEntity<List<MovieResponse>> getMoviesByGenre(@PathVariable("genreId") Long genreId) {
        log.info("Received request to find movies by genre with id: {}.", genreId);
        List<MovieResponse> movies = movieService.getMoviesByGenre(genreId);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/random")
    @Override
    public ResponseEntity<List<MovieResponse>> getRandomMovies() {
        log.info("Received request to find random movies.");
        List<MovieResponse> response = movieService.getRandomMovies();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}