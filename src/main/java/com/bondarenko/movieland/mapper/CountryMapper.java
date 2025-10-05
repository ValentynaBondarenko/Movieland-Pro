package com.bondarenko.movieland.mapper;

import com.bondarenko.movieland.api.model.CountryResponse;
import com.bondarenko.movieland.entity.Country;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CountryMapper {

    List<CountryResponse> toCountriesResponse(List<Country> countries);

    List<Country> toCountry(List<CountryResponse> responseList);
}
