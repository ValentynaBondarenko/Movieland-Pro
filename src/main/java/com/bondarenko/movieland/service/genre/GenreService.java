package com.bondarenko.movieland.service.genre;

import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.entity.Genre;

import java.util.List;

public interface GenreService {
    List<GenreResponse> findAll();

    List<GenreResponse> findByMovieId(Long movieId);

    List<GenreResponse> findByIdIn(List<Long> genreIds);
    List<Genre> findById(List<Long> genreIds);
}
