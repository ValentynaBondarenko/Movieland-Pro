package com.bondarenko.movieland.service.enrichment;

import com.bondarenko.movieland.api.model.CountryResponse;
import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.api.model.MovieRequest;
import com.bondarenko.movieland.entity.Movie;
import com.bondarenko.movieland.service.country.CountryService;
import com.bondarenko.movieland.service.genre.GenreService;
import com.bondarenko.movieland.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrichmentServiceImpl implements EnrichmentService {
    private final CountryService countryService;
    private final GenreService genreService;

    @Override
    public MovieRequest enrichMovie(MovieRequest movieRequest) {
        List<Long> genreIds = movieRequest.getGenres().stream()
                .map(GenreResponse::getId)
                .toList();
        List<GenreResponse> genresDTO = genreService.findByIdIn(genreIds);

        List<Long> countryIds = movieRequest.getCountries().stream()
                .map(CountryResponse::getId)
                .toList();

        List<CountryResponse> countriesDTO = countryService.findByIdIn(countryIds);

        movieRequest.setGenres(genresDTO);
        movieRequest.setCountries(countriesDTO);

        return movieRequest;
    }
}