package com.bondarenko.movieland.exception;

public class TimeoutEnrichMovieException extends RuntimeException {
    public TimeoutEnrichMovieException(String timeoutOccurredDuringMovieEnrichment) {
        super(timeoutOccurredDuringMovieEnrichment);
    }
}
