package com.bondarenko.movieland.exception;

public class MovieNotFoundException extends RuntimeException {

    private static final String NO_MOVIE_MESSAGE = "There is no movie";

    public MovieNotFoundException() {
        super(String.format(NO_MOVIE_MESSAGE));
    }

    public MovieNotFoundException(String message) {
        super();
    }
}