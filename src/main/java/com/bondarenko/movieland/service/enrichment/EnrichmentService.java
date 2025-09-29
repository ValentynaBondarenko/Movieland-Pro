package com.bondarenko.movieland.service.enrichment;

import com.bondarenko.movieland.api.model.MovieDto;

public interface EnrichmentService {
    void enrichMovie(MovieDto movieDto);

}
