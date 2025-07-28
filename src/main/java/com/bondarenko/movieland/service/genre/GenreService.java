package com.bondarenko.movieland.service.genre;

import com.bondarenko.movieland.api.model.GenreResponse;

import java.util.List;

public interface GenreService {
    List<GenreResponse> findAll();

    List<GenreResponse> findByMovieId(Long movieId);

    List<GenreResponse> findByIdIn(List<Long> genreIds);
}
