package com.bondarenko.movieland.service.genre;


import com.bondarenko.movieland.api.model.ResponseGenreDTO;

import java.util.List;

public interface GenreService {
    List<ResponseGenreDTO> getAllGenres();
}
