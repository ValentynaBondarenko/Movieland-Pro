package com.bondarenko.movieland.controller;

import com.bondarenko.movieland.dto.ResponseGenreDTO;
import com.bondarenko.movieland.service.GenreService;
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
@RequestMapping(path = "/api/v1/genre", produces = MediaType.APPLICATION_JSON_VALUE)
public class GenreController {
    private GenreService genreService;

    @GetMapping
    protected ResponseEntity<List<ResponseGenreDTO>> findAll() {
        List<ResponseGenreDTO> response = genreService.getAllGenres();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
