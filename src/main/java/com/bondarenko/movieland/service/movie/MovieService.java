package com.bondarenko.movieland.service.movie;

import com.bondarenko.movieland.api.model.ResponseMovieDTO;
import com.bondarenko.movieland.api.model.MovieSortCriteria;
import java.util.List;

public interface MovieService {
    List<ResponseMovieDTO> findAllMovies();

    List<ResponseMovieDTO> getRandomMovies();

    List<ResponseMovieDTO> getMoviesByGenre(int genreId);

    List<ResponseMovieDTO> findAllMoviesWithSorting(MovieSortCriteria movieSortCriteria);
}
