package com.bondarenko.movieland.service.movie;

import com.bondarenko.movieland.api.model.MovieRequest;
import com.bondarenko.movieland.api.model.MovieSortCriteria;
import com.bondarenko.movieland.api.model.ResponseFullMovie;
import com.bondarenko.movieland.api.model.ResponseMovie;

import java.util.List;

public interface MovieService {
    List<ResponseMovie> findAll(MovieSortCriteria movieSortCriteria);

    List<ResponseMovie> getRandomMovies();

    List<ResponseMovie> getMoviesByGenre(Integer genreId);

    void saveMovie(MovieRequest movieRequest);

    ResponseFullMovie updateMovie(Long id, MovieRequest movieRequest);

    ResponseFullMovie getMovieById(Long movieId, String currency);
}
