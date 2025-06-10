package com.bondarenko.movieland.service.genre;

import com.bondarenko.movieland.api.model.GenreDTO;
import com.bondarenko.movieland.entity.Genre;

import java.util.Set;

public interface GenreService {
    Set<GenreDTO> getAll();
    Genre getGenreById(Long genreId);

    Set<Genre> findByMovieId(Long movieId);

    Set<Genre> findByIdIn(Set<Long> genreIds);
}
