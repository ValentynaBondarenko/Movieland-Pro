package com.bondarenko.movieland.service;

import com.bondarenko.movieland.dto.RequestMovieDTO;
import com.bondarenko.movieland.entity.Movie;
import com.bondarenko.movieland.mapper.MovieMapper;
import com.bondarenko.movieland.repository.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {
    private MovieRepository movieRepository;
    private MovieMapper movieMapper;

    @Override
    public List<RequestMovieDTO> findAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movieMapper.toMovieDTO(movies);
    }

    @Override
    public List<RequestMovieDTO> getRandomMovies() {
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
}
