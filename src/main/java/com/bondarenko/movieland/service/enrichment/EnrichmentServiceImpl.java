package com.bondarenko.movieland.service.enrichment;

import com.bondarenko.movieland.api.model.MovieRequest;
import com.bondarenko.movieland.entity.Country;
import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.entity.Movie;
import com.bondarenko.movieland.service.country.CountryService;
import com.bondarenko.movieland.service.genre.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class EnrichmentServiceImpl implements EnrichmentService {
    private final CountryService countryService;
    private final GenreService genreService;

    @Override
    public Movie enrichMovie(Movie movie, MovieRequest movieRequest) {
        Set<Genre> genres = genreService.findByIdIn(movieRequest.getGenres());
        Set<Country> countries = countryService.findByIdIn(movieRequest.getCountries());

        return movie.setGenres(genres)
                .setCountries(countries);
    }

}