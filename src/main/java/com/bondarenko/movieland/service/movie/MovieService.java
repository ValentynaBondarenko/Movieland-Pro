package com.bondarenko.movieland.service.movie;

import com.bondarenko.movieland.api.model.ResponseMovie;
import com.bondarenko.movieland.api.model.MovieSortCriteria;
import java.util.List;

public interface MovieService {
    List<ResponseMovie> findAllMovies(MovieSortCriteria movieSortCriteria);

    List<ResponseMovie> getRandomMovies();

    List<ResponseMovie> getMoviesByGenre(int genreId);

}
