package com.bondarenko.movieland.service.country;

import com.bondarenko.movieland.entity.Country;
import com.bondarenko.movieland.repository.CountryRepository;
import com.bondarenko.movieland.service.cache.CountryCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryCache countryCache;
    private final CountryRepository countryRepository;

    @Override
    public Set<Country> findByIdIn(Set<Long> countryIds) {
        return countryCache.getCountries().stream()
                .filter(country -> countryIds.contains(country.getId()))
                .collect(Collectors.toSet());
    }

    /**
     * Retrieves the set of countries associated with the given movie ID.
     * This method assumes that country data is stored in a SEPARATE database
     * or external data source
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @Override
    public Set<Country> findByMovieId(Long id) {
        return countryRepository.findByMovieId(id);
    }


}
