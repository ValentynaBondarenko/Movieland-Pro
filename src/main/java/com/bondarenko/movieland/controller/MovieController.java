package com.bondarenko.movieland.controller;

import com.bondarenko.movieland.dto.RequestMovieDTO;
import com.bondarenko.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/movie", produces = MediaType.APPLICATION_JSON_VALUE)
public class MovieController {
    private MovieService movieService;

    @GetMapping
    protected ResponseEntity<List<RequestMovieDTO>> findAll() {
        List<RequestMovieDTO> response = movieService.findAllMovies();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
