package com.bondarenko.movieland.service.genre;

import com.bondarenko.movieland.api.model.ResponseGenre;
import com.bondarenko.movieland.service.cache.GenreCacheAsideService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreCacheAsideService genreCache;

    @Override
    public List<ResponseGenre> getAll() {
        return genreCache.getGenre();
    }
}