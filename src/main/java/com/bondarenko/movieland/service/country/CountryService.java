package com.bondarenko.movieland.service.country;

import com.bondarenko.movieland.api.model.CountryResponse;

import java.util.List;

public interface CountryService {
    List<CountryResponse> findByIdIn(List<Long> countryIds);

    List<CountryResponse> findByMovieId(Long id);

    List<CountryResponse> findAll();
}
