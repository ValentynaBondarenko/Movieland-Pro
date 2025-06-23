package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.entity.Country;
import com.bondarenko.movieland.repository.CountryRepository;
import com.bondarenko.movieland.service.annotation.CacheService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@CacheService
@RequiredArgsConstructor
public class CountryCache {
    private final CountryRepository countryRepository;
    private Cache<Country> countryCache;

    public List<Country> getCountries() {
        return countryCache.getAll();
    }

    @PostConstruct
    private void init() {
        countryCache = new Cache<>(countryRepository::findAll);
        countryCache.refresh();
    }

    @Scheduled(fixedDelayString = "${movieland.cache.update.interval}")
    private void refreshCache() {
        countryCache.refresh();
    }
}

