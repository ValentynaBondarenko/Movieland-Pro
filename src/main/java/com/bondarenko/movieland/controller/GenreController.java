package com.bondarenko.movieland.controller;

import com.bondarenko.movieland.api.GenresApi;
import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.api.model.MovieResponse;
import com.bondarenko.movieland.service.genre.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1")
public class GenreController implements GenresApi {
    private final GenreService genreService;

    @Override
    public ResponseEntity<Set<GenreResponse>> findAllGenres() {
        log.info("Received request to find all genres.");
        Set<GenreResponse> response = genreService.getAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
