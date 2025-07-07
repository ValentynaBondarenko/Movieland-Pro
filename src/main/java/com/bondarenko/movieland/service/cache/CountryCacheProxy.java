package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.entity.Country;
import com.bondarenko.movieland.repository.CountryRepository;
import com.bondarenko.movieland.service.annotation.CacheService;
import com.bondarenko.movieland.service.country.CountryService;
import com.bondarenko.movieland.service.country.CountryServiceImpl;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CacheService
@RequiredArgsConstructor
public class CountryCacheProxy implements CountryService {
    private final CountryServiceImpl countryServiceImpl;
    private Cache<Country> countryCache;

    @PostConstruct
    private void init() {
        countryCache = new Cache<>(countryServiceImpl::findAll);
        countryCache.refresh();
    }

    @Scheduled(fixedDelayString = "${movieland.cache.update.interval}")
    private void refreshCache() {
        countryCache.refresh();
    }

    @Override
    public Set<Country> findByIdIn(Set<Long> countryIds) {
        return countryCache.getAll().stream()
                .filter(country -> countryIds.contains(country.getId()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Country> findByMovieId(Long id) {
        Set<Country> countries = countryServiceImpl.findByMovieId(id);
        countryCache.addIfNotPresent(new ArrayList<>(countries));
        return countries;
    }

    @Override
    public List<Country> findAll() {
        return countryCache.getAll();
    }

}

