package com.bondarenko.movieland.service;

import com.bondarenko.movieland.dto.ResponseGenreDTO;
import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.exception.GenreNotFoundException;
import com.bondarenko.movieland.mapper.GenreMapper;
import com.bondarenko.movieland.repository.GenreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {
    private GenreMapper genreMapper;
    private GenreRepository genreRepository;

    @Override
    public List<ResponseGenreDTO> getAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        return Optional.ofNullable(genres)
                .map(genreMapper::toGenreDTO)
                .orElseThrow(GenreNotFoundException::new);
    }
}