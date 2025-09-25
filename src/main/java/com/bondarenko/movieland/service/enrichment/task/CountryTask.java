package com.bondarenko.movieland.service.enrichment.task;

import com.bondarenko.movieland.api.model.CountryResponse;
import com.bondarenko.movieland.api.model.FullMovieResponse;
import com.bondarenko.movieland.api.model.MovieRequest;
import com.bondarenko.movieland.service.country.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CountryTask implements Runnable {
    private final CountryService countryService;
    @Setter
    private FullMovieResponse fullMovieResponse;

    @Override
    public void run() {
        List<Long> countriesIds = Optional.ofNullable(fullMovieResponse.getCountries())
                .orElse(List.of())
                .stream()
                .map(CountryResponse::getId)
                .toList();
        System.out.println("Country IDs: " + countriesIds);

        List<CountryResponse> countries = countryService.findByIdIn(countriesIds);
        fullMovieResponse.setCountries(countries);
    }
}
