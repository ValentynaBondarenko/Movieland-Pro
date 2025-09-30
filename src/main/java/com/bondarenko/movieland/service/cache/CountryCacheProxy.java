package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.api.model.CountryResponse;
import com.bondarenko.movieland.entity.Country;
import com.bondarenko.movieland.mapper.CountryMapper;
import com.bondarenko.movieland.service.annotation.CacheService;
import com.bondarenko.movieland.service.country.CountryService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
@CacheService
@RequiredArgsConstructor
public class CountryCacheProxy implements CountryService {
    private final CountryService countryService;
    private Cache<CountryResponse> countryCache;
    private final CountryMapper countryMapper;

    @PostConstruct
    //A @PostConstruct method must not have any parameters, must not throw a checked exception, and must return void.
    //method.invoke(bean);
    //@PostConstruct is meant for simple technical setup, not business logic.
    private void init() {
        countryCache = new Cache<>(countryService::findAll);
        countryCache.refresh();
        log.info("Country cache initialized, size= {}", countryCache.getAll().size());
    }

    @Scheduled(fixedDelayString = "${movieland.cache.update.interval}")
    //method.invoke(bean); without params
    private void refreshCache() {
        countryCache.refresh();
        log.info("Refresh country cache, size= {}", countryCache.getAll().size());
    }

    @Override
    public List<CountryResponse> findByIdIn(List<Long> countryIds) {
        return countryCache.getAll().stream()
                .filter(country -> countryIds.contains(country.getId()))
                .toList();
    }

    @Override
    public List<Country> findById(List<Long> countryIds) {
        List<CountryResponse> responseList = countryCache.getAll().stream()
                .filter(country -> countryIds.contains(country.getId()))
                .toList();
        return countryMapper.toCountry(responseList);
    }

    @Override
    public List<CountryResponse> findByMovieId(Long id) {
        return countryService.findByMovieId(id);
    }

    @Override
    public List<CountryResponse> findAll() {
        return countryCache.getAll();
    }

}

