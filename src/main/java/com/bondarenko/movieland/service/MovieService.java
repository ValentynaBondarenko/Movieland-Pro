package com.bondarenko.movieland.service;

import com.bondarenko.movieland.dto.RequestMovieDTO;

import java.util.List;

public interface MovieService {
     List<RequestMovieDTO> findAllMovies() ;
}
