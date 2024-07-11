package com.bondarenko.movieland.exception;

public class GenreNotFoundException extends RuntimeException {

    private static final String NO_GENRE_BY_ID_MESSAGE = "There is no genre";

    public GenreNotFoundException() {
        super(String.format(NO_GENRE_BY_ID_MESSAGE));
    }

    public GenreNotFoundException(String message) {
        super();
    }
}
