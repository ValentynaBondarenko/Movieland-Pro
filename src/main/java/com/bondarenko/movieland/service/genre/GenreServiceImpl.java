package com.bondarenko.movieland.service.genre;

import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.exception.GenreNotFoundException;
import com.bondarenko.movieland.mapper.GenreMapper;
import com.bondarenko.movieland.repository.GenreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    public List<GenreResponse> findAll() {
        List<Genre> genres = genreRepository.findAll();
        if (genres.isEmpty()) {
            throw new GenreNotFoundException("Can't find genres");
        }
        return genreMapper.toGenreResponse(genres);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @Override
    public List<GenreResponse> findByMovieId(Long movieId) {
        List<Genre> genres = genreRepository.findByMovieId(movieId);
        if (genres == null || genres.isEmpty()) {
            throw new GenreNotFoundException("Can't find genres by movie id: " + movieId);
        }
        return genreMapper.toGenreResponse(genres);
    }

    @Override
    public List<GenreResponse> findByIdIn(List<Long> genreIds) {
        List<Genre> genres = genreRepository.findAll();
        List<Genre> collect = genres.stream()
                .filter(genre -> genreIds.contains((genre.getId())))
                .toList();
        return genreMapper.toGenreResponse(collect);
    }
}