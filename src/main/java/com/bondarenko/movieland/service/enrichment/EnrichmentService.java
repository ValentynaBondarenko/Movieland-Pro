package com.bondarenko.movieland.service.enrichment;

import com.bondarenko.movieland.api.dto.MovieRequest;
import com.bondarenko.movieland.entity.Movie;

public interface EnrichmentService {
    Movie enrichMovie(Movie movie, MovieRequest movieDto);

}
