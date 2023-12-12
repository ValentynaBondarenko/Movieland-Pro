package com.bondarenko.movieland.controller;

import com.bondarenko.movieland.api.GenreApi;
import com.bondarenko.movieland.api.model.ResponseGenreDTO;
import com.bondarenko.movieland.service.genre.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/genre", produces = MediaType.APPLICATION_JSON_VALUE)
public class GenreController implements GenreApi {
    private GenreService genreService;

    @GetMapping
    public ResponseEntity<List<ResponseGenreDTO>> findAllGenres() {
        log.info("Received request to find all genres.");
        List<ResponseGenreDTO> response = genreService.getAllGenres();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
