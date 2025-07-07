package com.bondarenko.movieland.service.genre;

import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.entity.Genre;

import java.util.Set;

public interface GenreService {
    Set<GenreResponse> findAll();

    //for cache
    Genre getGenreById(Long genreId);

    Set<Genre> findByMovieId(Long movieId);

    Set<Genre> findByIdIn(Set<Long> genreIds);
}
