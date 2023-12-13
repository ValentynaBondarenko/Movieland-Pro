package com.bondarenko.movieland.util;

import com.bondarenko.movieland.api.model.ResponseGenreDTO;

import java.util.Arrays;
import java.util.List;

public class TestGenres {
    public static List<ResponseGenreDTO> getGenres() {
        return Arrays.asList(
                createGenre(1, "Драма"),
                createGenre(2, "Кримінал"),
                createGenre(3, "Фентезі")
        );
    }

    private static ResponseGenreDTO createGenre(int id, String genreName) {
        ResponseGenreDTO genre = new ResponseGenreDTO();
        genre.setId(id);
        genre.setName(genreName);
        return genre;
    }

}
