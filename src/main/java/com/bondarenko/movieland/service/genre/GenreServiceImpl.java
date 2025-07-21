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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    public Set<GenreResponse> findAll() {
        List<Genre> genres = genreRepository.findAll();
        if (genres.isEmpty()) {
            throw new GenreNotFoundException("Can't find genres");
        }
        return genreMapper.toGenreResponse(new HashSet<>(genres));
    }

    @Override
    public GenreResponse getGenreById(Long genreId) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new GenreNotFoundException("Can't find genre by id: " + genreId));
        return genreMapper.toGenreResponse(genre);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @Override
    public Set<GenreResponse> findByMovieId(Long movieId) {
        Set<Genre> genres = genreRepository.findByMovieId(movieId);
        if (genres == null || genres.isEmpty()) {
            throw new GenreNotFoundException("Can't find genres by movie id: " + movieId);
        }
        return genreMapper.toGenreResponse(genres);
    }

    @Override
    public Set<GenreResponse> findByIdIn(Set<Long> genreIds) {
        List<Genre> genres = genreRepository.findAll();
        Set<Genre> collect = genres.stream()
                .filter(genre -> genreIds.contains((genre.getId())))
                .collect(Collectors.toSet());
        return genreMapper.toGenreResponse(new HashSet<>(collect));
    }
}