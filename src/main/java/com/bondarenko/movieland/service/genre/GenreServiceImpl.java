package com.bondarenko.movieland.service.genre;

import com.bondarenko.movieland.api.model.ResponseGenre;
import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.exception.GenreNotFoundException;
import com.bondarenko.movieland.mapper.GenreMapper;
import com.bondarenko.movieland.repository.GenreRepository;
import com.bondarenko.movieland.service.cache.GenreCache;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
    public Set<ResponseGenre> getAll() {
        return Optional.of(genreCache.getGenres())
                .map(genreMapper::toGenreResponse)
                .orElseThrow(() -> new GenreNotFoundException("Can't find genre"));
    }

    @Override
    public Genre getGenreById(Long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new GenreNotFoundException("Can't find genre by id: " + genreId));
    }

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