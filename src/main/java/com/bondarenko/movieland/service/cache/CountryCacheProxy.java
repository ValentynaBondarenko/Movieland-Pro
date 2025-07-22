package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.api.model.CountryResponse;
import com.bondarenko.movieland.service.annotation.CacheService;
import com.bondarenko.movieland.service.country.CountryService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CacheService
@RequiredArgsConstructor
public class CountryCacheProxy implements CountryService {
    private final CountryService countryService;
    private Cache<CountryResponse> countryCache;

    @PostConstruct
    //A @PostConstruct method must not have any parameters, must not throw a checked exception, and must return void.
    //method.invoke(bean);
    //@PostConstruct is meant for simple technical setup, not business logic.
    private void init() {
        countryCache = new Cache<>(countryService::findAll);
        countryCache.refresh();
    }

    @Scheduled(fixedDelayString = "${movieland.cache.update.interval}")
    //method.invoke(bean); without params
    private void refreshCache() {
        countryCache.refresh();
    }

    @Override
    public Set<CountryResponse> findByIdIn(Set<Long> countryIds) {
        return countryCache.getAll().stream()
                .filter(country -> countryIds.contains(country.getId()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<CountryResponse> findByMovieId(Long id) {
        Set<CountryResponse> countryResponses = countryService.findByMovieId(id);
        countryCache.addIfNotPresent(new ArrayList<>(countryResponses));
        return countryResponses;
    }

    @Override
    public List<CountryResponse> findAll() {
        return countryCache.getAll();
    }

}

