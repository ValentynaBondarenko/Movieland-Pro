package com.bondarenko.movieland.controller;

import com.bondarenko.movieland.api.MovieApi;
import com.bondarenko.movieland.api.model.MovieSortCriteria;
import com.bondarenko.movieland.api.model.ResponseFullMovie;
import com.bondarenko.movieland.api.model.ResponseMovie;
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
    public ResponseEntity<List<ResponseMovie>> findAllMovies(MovieSortCriteria movieSortCriteria) {
        List<ResponseMovie> response = movieService.findAllMovies(movieSortCriteria);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseFullMovie> getMovieById(Integer movieId) {
        ResponseFullMovie fullMovie = movieService.getMovieById(movieId);
        return ResponseEntity.ok(fullMovie);
    }

    public ResponseEntity<List<ResponseMovie>> getMoviesByGenre(Integer genreId) {
        log.info("Received request to find movies by genre with ID {}.", genreId);
        List<ResponseMovie> movies = movieService.getMoviesByGenre(genreId);
        return ResponseEntity.ok(movies);
    }

    public ResponseEntity<List<ResponseMovie>> getRandomMovies() {
        log.info("Received request to find random movies.");
        List<ResponseMovie> response = movieService.getRandomMovies();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}