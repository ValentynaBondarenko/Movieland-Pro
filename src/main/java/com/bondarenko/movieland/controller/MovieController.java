package com.bondarenko.movieland.controller;

import com.bondarenko.movieland.api.MovieApi;
import com.bondarenko.movieland.api.model.MovieRequest;
import com.bondarenko.movieland.api.model.MovieSortCriteria;
import com.bondarenko.movieland.api.model.ResponseFullMovie;
import com.bondarenko.movieland.api.model.ResponseMovie;
import com.bondarenko.movieland.service.movie.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1")
public class MovieController implements MovieApi {
    private final MovieService movieService;

    @Override
    public ResponseEntity<Void> addMovie(MovieRequest movieRequest) {
        log.info("Received request to add new movie {}", movieRequest);
        movieService.saveMovie(movieRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> editMovie(Integer id, MovieRequest movieRequest) {
        log.info("Received request to edit by id={} movie {}", id, movieRequest);
        movieService.updateMovie(id, movieRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ResponseMovie>> findAllMovies(MovieSortCriteria movieSortCriteria) {
        log.info("Received request to find all movies ");
        List<ResponseMovie> response = movieService.findAll(movieSortCriteria);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseFullMovie> getMovieById(Integer movieId) {
        log.info("Received request to get movie by id: {}.", movieId);
        ResponseFullMovie fullMovie = movieService.getMovieById(movieId);
        return ResponseEntity.ok(fullMovie);
    }

    public ResponseEntity<List<ResponseMovie>> getMoviesByGenre(Integer genreId) {
        log.info("Received request to find movies by genre with id: {}.", genreId);
        List<ResponseMovie> movies = movieService.getMoviesByGenre(genreId);
        return ResponseEntity.ok(movies);
    }

    public ResponseEntity<List<ResponseMovie>> getRandomMovies() {
        log.info("Received request to find random movies.");
        List<ResponseMovie> response = movieService.getRandomMovies();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}