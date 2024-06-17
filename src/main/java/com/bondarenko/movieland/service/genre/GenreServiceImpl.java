package com.bondarenko.movieland.service.genre;

import com.bondarenko.movieland.api.model.*;
import com.bondarenko.movieland.service.cache.*;
import lombok.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreCacheAsideService genreCache;

    @Override
    public List<ResponseGenre> getAll() {
        return genreCache.getGenres();
    }
}