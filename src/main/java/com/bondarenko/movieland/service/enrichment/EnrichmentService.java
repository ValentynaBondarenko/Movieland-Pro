package com.bondarenko.movieland.service.enrichment;

import com.bondarenko.movieland.api.model.MovieRequest;

public interface EnrichmentService {
    void enrichMovie(MovieRequest movieDto);

}
