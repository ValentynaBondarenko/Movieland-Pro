package com.bondarenko.movieland.service.movie;

import com.bondarenko.movieland.api.model.ResponseMovieDTO;
import com.bondarenko.movieland.entity.Movie;
import com.bondarenko.movieland.exception.MovieNotFoundException;
import com.bondarenko.movieland.mapper.MovieMapper;
import com.bondarenko.movieland.repository.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {
    private MovieRepository movieRepository;
    private MovieMapper movieMapper;

    @Override
    public List<ResponseMovieDTO> findAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return Optional.of(movies)
                .map(movieMapper::toMovieDTO)
                .orElseThrow(MovieNotFoundException::new);
    }

    @Override
    public List<ResponseMovieDTO> getRandomMovies() {
        List<Movie> allMovies = movieRepository.findAll();
        List<Movie> randomMovies = new ArrayList<>();

        if (allMovies.size() >= 3) {
            var random = new Random();
            while (randomMovies.size() < 3) {
                int randomIndex = random.nextInt(allMovies.size());
                Movie randomMovie = allMovies.get(randomIndex);
                randomMovies.add(randomMovie);
                allMovies.remove(randomIndex);
            }
        } else {
            randomMovies.addAll(allMovies);
        }
        return movieMapper.toMovieDTO(randomMovies);
    }

    @Override
    public List<ResponseMovieDTO> getMoviesByGenre(int genreId) {
        List<Movie> movies = movieRepository.findByGenres_Id(genreId);
        return Optional.of(movies)
                .map(movieMapper::toMovieDTO)
                .orElseThrow(MovieNotFoundException::new);
    }
}
