package com.bondarenko.movieland.controller;

import com.bondarenko.movieland.api.*;
import com.bondarenko.movieland.api.model.*;
import com.bondarenko.movieland.service.movie.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1")
public class MovieController implements MovieApi {
    private final MovieService movieService;

    @Override
    @RequestMapping(produces = {"application/json"})
    public ResponseEntity<List<ResponseMovie>> findAllMovies(MovieSortCriteria movieSortCriteria) {
        log.info("Received request to find all movies ");
        List<ResponseMovie> response = movieService.findAll(movieSortCriteria);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseFullMovie> getMovieById(Integer movieId, String currency) {

        log.info("Received request to get movie by id: {}. And currency: {} ", movieId, currency);
        ResponseFullMovie fullMovie = movieService.getMovieById(movieId, currency);
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