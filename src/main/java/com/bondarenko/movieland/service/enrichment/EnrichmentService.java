package com.bondarenko.movieland.service.enrichment;

import com.bondarenko.movieland.api.model.FullMovieResponse;

public interface EnrichmentService {
    void enrichMovie(FullMovieResponse movieDto);

}
