package com.bondarenko.movieland.service;

import com.bondarenko.movieland.dto.ResponseGenreDTO;

import java.util.List;

public interface GenreService {
    List<ResponseGenreDTO> getAllGenres();
}
