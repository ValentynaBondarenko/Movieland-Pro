package com.bondarenko.movieland.util;

import com.bondarenko.movieland.api.model.ResponseGenre;

import java.util.Arrays;
import java.util.List;

public class TestGenres {
    public static List<ResponseGenre> getGenres() {
        return Arrays.asList(
                createGenre(1, "Драма"),
                createGenre(2, "Кримінал"),
                createGenre(3, "Фентезі")
        );
    }

    private static ResponseGenre createGenre(int id, String genreName) {
        ResponseGenre genre = new ResponseGenre();
        genre.setId(id);
        genre.setName(genreName);
        return genre;
    }

}
