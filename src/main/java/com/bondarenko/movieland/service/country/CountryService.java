package com.bondarenko.movieland.service.country;

import com.bondarenko.movieland.entity.Country;

import java.util.Set;

public interface CountryService {
    Set<Country> findByIdIn(Set<Long> countryIds);
}
