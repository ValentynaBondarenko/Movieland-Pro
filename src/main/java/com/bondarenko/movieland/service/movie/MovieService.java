package com.bondarenko.movieland.service.movie;

import com.bondarenko.movieland.api.model.FullMovieResponse;
import com.bondarenko.movieland.api.model.MovieDto;
import com.bondarenko.movieland.api.model.MovieResponse;
import com.bondarenko.movieland.api.model.MovieSortRequest;
import com.bondarenko.movieland.entity.CurrencyType;

import java.util.List;

public interface MovieService {
    List<MovieResponse> findAll(MovieSortRequest movieSortRequest);

    List<MovieResponse> getRandomMovies();

    List<MovieResponse> getMoviesByGenre(Long genreId);

    void saveMovie(MovieDto MovieDto);

    FullMovieResponse updateMovie(Long id, MovieDto MovieDto);

    FullMovieResponse getMovieById(Long movieId, CurrencyType currency);
}
