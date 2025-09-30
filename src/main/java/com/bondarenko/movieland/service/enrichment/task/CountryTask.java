package com.bondarenko.movieland.service.enrichment.task;

import com.bondarenko.movieland.entity.Country;
import com.bondarenko.movieland.entity.Movie;
import com.bondarenko.movieland.service.country.CountryService;
import com.bondarenko.movieland.util.TimeLoggerUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CountryTask implements Runnable {
    private final CountryService countryService;
    @Setter
    private Movie movie;

    @Override
    public void run() {
        long start = TimeLoggerUtil.start("Country");

        List<Long> countryIds = movie.getCountries().stream()
                .map(Country::getId)
                .toList();

        List<Country> countries = new ArrayList<>(countryService.findById(countryIds));
        movie.setCountries(countries);

        TimeLoggerUtil.end("Country", start);
        log.info("Country task finished with countries: {}", countries);
    }

}
