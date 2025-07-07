package com.bondarenko.movieland.service.country;

import com.bondarenko.movieland.entity.Country;

import java.util.List;
import java.util.Set;

public interface CountryService {
    Set<Country> findByIdIn(Set<Long> countryIds);

    Set<Country> findByMovieId(Long id);

    List<Country> findAll();
}
