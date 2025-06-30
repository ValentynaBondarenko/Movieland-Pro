package com.bondarenko.movieland.service.movie;

import com.bondarenko.movieland.api.dto.FullMovieResponse;
import com.bondarenko.movieland.api.dto.MovieRequest;
import com.bondarenko.movieland.api.dto.MovieResponse;
import com.bondarenko.movieland.api.dto.MovieSortRequest;
import com.bondarenko.movieland.entity.CurrencyType;

import java.util.List;

public interface MovieService {
    List<MovieResponse> findAll(MovieSortRequest MovieSortRequest);

    List<MovieResponse> getRandomMovies();

    List<MovieResponse> getMoviesByGenre(Long genreId);

    void saveMovie(MovieRequest movieRequest);

    FullMovieResponse updateMovie(Long id, MovieRequest movieRequest);

    FullMovieResponse getMovieById(Long movieId, CurrencyType currency);
}
