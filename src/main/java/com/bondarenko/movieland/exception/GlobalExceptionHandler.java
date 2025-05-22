package com.bondarenko.movieland.exception;

import com.bondarenko.movieland.api.model.ProblemDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GenreNotFoundException.class)
    public ResponseEntity<ProblemDetails> handleGenreNotFoundException(GenreNotFoundException ex) {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setStatus(HttpStatus.NOT_FOUND.value());
        problemDetails.setTitle("There is no genre");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetails);
    }
}
