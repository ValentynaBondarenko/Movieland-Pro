package com.bondarenko.movieland.service.country;

import com.bondarenko.movieland.api.model.CountryResponse;
import com.bondarenko.movieland.entity.Country;

import java.util.List;
import java.util.Set;

public interface CountryService {
    Set<CountryResponse> findByIdIn(Set<Long> countryIds);

    Set<CountryResponse> findByMovieId(Long id);

    List<CountryResponse> findAll();
}
