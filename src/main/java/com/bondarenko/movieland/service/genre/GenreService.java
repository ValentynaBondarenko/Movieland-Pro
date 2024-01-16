package com.bondarenko.movieland.service.genre;


import com.bondarenko.movieland.api.model.ResponseGenre;

import java.util.List;

public interface GenreService {
    List<ResponseGenre> getAllGenres();
}
