package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.entity.Country;
import com.bondarenko.movieland.repository.CountryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CountryCache {
    private final CountryRepository countryRepository;
    private CustomCache<Country> countryCache;

    @PostConstruct
    public void init() {
        countryCache = new CustomCache<>(countryRepository::findAll);
        countryCache.refresh();
    }

    public Set<Country> getCountries() {
        return countryCache.getAll();
    }
}

