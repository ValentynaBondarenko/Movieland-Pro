package com.bondarenko.movieland.service;

import com.bondarenko.movieland.dto.RequestMovieDTO;
import com.bondarenko.movieland.entity.Movie;
import com.bondarenko.movieland.mapper.MovieMapper;
import com.bondarenko.movieland.repository.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
