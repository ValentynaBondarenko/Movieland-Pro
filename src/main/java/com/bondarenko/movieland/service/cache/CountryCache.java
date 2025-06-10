package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.entity.Country;
import com.bondarenko.movieland.repository.CountryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CountryCache {
    private final CountryRepository countryRepository;
    private Cache<Country> countryCache;

    public Set<Country> getCountries() {
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

