package com.bondarenko.movieland.mapper;

import com.bondarenko.movieland.api.model.CountryResponse;
import com.bondarenko.movieland.entity.Country;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CountryMapper {
    Set<Country> toCountries(Set<CountryResponse> countries);

    Set<CountryResponse> toCountriesResponse(Set<Country> countries);
}
