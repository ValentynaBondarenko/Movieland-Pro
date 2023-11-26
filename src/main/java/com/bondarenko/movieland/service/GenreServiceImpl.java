package com.bondarenko.movieland.service;

import com.bondarenko.movieland.dto.ResponseGenreDTO;
import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.mapper.GenreMapper;
import com.bondarenko.movieland.repository.GenreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {
    private GenreMapper genreMapper;
    private GenreRepository genreRepository;

    @Override
    public List<ResponseGenreDTO> getAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        return genreMapper.toGenreDTO(genres);

    }
}
