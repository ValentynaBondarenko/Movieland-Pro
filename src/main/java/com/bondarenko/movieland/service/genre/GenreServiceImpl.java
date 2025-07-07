package com.bondarenko.movieland.service.genre;

import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.exception.GenreNotFoundException;
import com.bondarenko.movieland.mapper.GenreMapper;
import com.bondarenko.movieland.repository.GenreRepository;
import com.bondarenko.movieland.service.cache.GenreCache;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreCache genreCache;
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    public Set<GenreResponse> getAll() {
        return Optional.of(genreCache.getGenres())
                .map(list -> genreMapper.toGenreResponse(new HashSet<>(list)))
                .orElseThrow(() -> new GenreNotFoundException("Can't find genre"));
    }

    @Override
    public Genre getGenreById(Long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new GenreNotFoundException("Can't find genre by id: " + genreId));
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW,readOnly = true)
    @Override
    public Set<Genre> findByMovieId(Long movieId) {
        Set<Genre> genres = genreRepository.findByMovieId(movieId);
        if (genres == null || genres.isEmpty()) {
            throw new GenreNotFoundException("Can't find genres by movie id: " + movieId);
        }
        return genres;
    }

    @Override
    public Set<Genre> findByIdIn(Set<Long> genreIds) {
        return genreCache.getGenres().stream()
                .filter(genre -> genreIds.contains(genre.getId()))
                .collect(Collectors.toSet());
    }
}