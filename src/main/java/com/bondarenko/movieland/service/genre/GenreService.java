package com.bondarenko.movieland.service.genre;

import com.bondarenko.movieland.api.model.ResponseGenre;

import java.util.List;
import java.util.Set;

public interface GenreService {
    Set<ResponseGenre> getAll();
}
