package com.bondarenko.movieland.service.enrichment;

import com.bondarenko.movieland.api.model.MovieRequest;
import com.bondarenko.movieland.entity.Movie;

public interface EnrichmentService {
    Movie enrichMovie(Movie movie, MovieRequest movieDto);

    void enrichMovie(Movie movie);

}
