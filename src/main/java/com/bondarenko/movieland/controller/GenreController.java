package com.bondarenko.movieland.controller;

import com.bondarenko.movieland.api.GenreApi;
import com.bondarenko.movieland.api.model.ResponseGenre;
import com.bondarenko.movieland.service.genre.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1")
public class GenreController implements GenreApi {
    private final GenreService genreService;

    @GetMapping
    public ResponseEntity<List<ResponseGenre>> findAllGenres() {
        log.info("Received request to find all genres.");
        List<ResponseGenre> response = genreService.getAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
