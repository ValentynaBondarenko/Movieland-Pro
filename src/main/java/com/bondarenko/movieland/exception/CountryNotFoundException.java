package com.bondarenko.movieland.exception;

public class CountryNotFoundException extends RuntimeException {

    private static final String NO_COUNTRY_BY_ID_MESSAGE = "There is no country";

    public CountryNotFoundException() {
        super(String.format(NO_COUNTRY_BY_ID_MESSAGE));
    }

    public CountryNotFoundException(String message) {
        super();
    }
}
