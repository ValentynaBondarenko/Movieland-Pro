package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.entity.Country;
import com.bondarenko.movieland.exception.CountryNotFoundException;
import com.bondarenko.movieland.repository.CountryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
@Slf4j
@Service
@RequiredArgsConstructor
public class CountryCache {
    private final CountryRepository countryRepository;
    private final CopyOnWriteArrayList<Country> countryCache = new CopyOnWriteArrayList<>();

    public Set<Country> getCountries() {
        if (countryCache.isEmpty()) {
            throw new CountryNotFoundException("Country not found");
        }
        return new HashSet<>(countryCache); //if not found update cache
    }

    @PostConstruct
    private void cacheLoading() {
        Set<Country> countries = fetchCountriesFromDatabase();
        countryCache.addAll(countries);
        log.info("Loaded {} countries cache successfully ", countries.size());
    }

    private Set<Country> fetchCountriesFromDatabase() {
        List<Country> countryList = countryRepository.findAll();
        return new HashSet<>(countryList);
    }
}
