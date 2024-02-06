package com.bondarenko.movieland.service.genre;

import com.bondarenko.movieland.api.model.ResponseGenre;
import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.exception.GenreNotFoundException;
import com.bondarenko.movieland.mapper.GenreMapper;
import com.bondarenko.movieland.service.cache.GenreCacheService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {
    private GenreMapper genreMapper;
    private GenreCacheService genreCache;

    @Override
    public List<ResponseGenre> getAllGenres() {
        List<Genre> genres = genreCache.getGenres();
        return Optional.ofNullable(genres)
                .map(genreMapper::toGenreResponse)
                .orElseThrow(GenreNotFoundException::new);
    }
}