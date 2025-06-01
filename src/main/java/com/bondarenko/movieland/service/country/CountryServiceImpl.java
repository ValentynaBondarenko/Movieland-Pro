package com.bondarenko.movieland.service.country;

import com.bondarenko.movieland.entity.Country;
import com.bondarenko.movieland.service.cache.CountryCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryCache countryCache;

    @Override
    public Set<Country> findByIdIn(Set<Long> countryIds) {
        return countryCache.getCountries().stream()
                .filter(country -> countryIds.contains(country.getId()))
                .collect(Collectors.toSet());
    }


}
