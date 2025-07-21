package com.bondarenko.movieland.service.genre;

import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.entity.Genre;

import java.util.Set;

public interface GenreService {
    Set<GenreResponse> findAll();

    //for cache
    GenreResponse getGenreById(Long genreId);

    Set<GenreResponse> findByMovieId(Long movieId);

    Set<GenreResponse> findByIdIn(Set<Long> genreIds);
}
