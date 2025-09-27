package com.bondarenko.movieland.service.enrichment.task;

import com.bondarenko.movieland.api.model.CountryResponse;
import com.bondarenko.movieland.api.model.MovieDto;
import com.bondarenko.movieland.service.country.CountryService;
import com.bondarenko.movieland.util.TimeLoggerUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CountryTask implements Runnable {
    private final CountryService countryService;
    private final String taskName = "COUNTRY";

    @Setter
    private MovieDto movieDto;

    @Override
    public void run() {
        long start = TimeLoggerUtil.start("Country");
        List<Long> countriesIds = Optional.ofNullable(movieDto.getCountries())
                .orElse(List.of())
                .stream()
                .map(CountryResponse::getId)
                .toList();

        List<CountryResponse> countries = countryService.findByIdIn(countriesIds);
        movieDto.setCountries(countries);
        TimeLoggerUtil.end("Country", start);
        log.info("Country task finished with countries: {}", countries);
    }

    public String getTaskName() {
        return taskName;
    }
}
