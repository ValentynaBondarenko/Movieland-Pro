package com.bondarenko.movieland.service.enrichment;

import com.bondarenko.movieland.api.model.CountryDTO;
import com.bondarenko.movieland.api.model.GenreDTO;
import com.bondarenko.movieland.api.model.MovieRequest;
import com.bondarenko.movieland.entity.Country;
import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.entity.Movie;
import com.bondarenko.movieland.service.country.CountryService;
import com.bondarenko.movieland.service.genre.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrichmentServiceImpl implements EnrichmentService {
    private final CountryService countryService;
    private final GenreService genreService;

    @Override
    public Movie enrichMovie(Movie movie, MovieRequest movieRequest) {
        Set<Long> genreIds = movieRequest.getGenres().stream()
                .map(GenreDTO::getId)
                .collect(Collectors.toSet());
        Set<Genre> genres = genreService.findByIdIn(genreIds);

        Set<Long> countryIds = movieRequest.getCountries().stream()
                .map(CountryDTO::getId)
                .collect(Collectors.toSet());
        Set<Country> countries = countryService.findByIdIn(countryIds);

        return movie.setGenres(genres)
                .setCountries(countries);
    }

}