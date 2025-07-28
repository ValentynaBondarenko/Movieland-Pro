package com.bondarenko.movieland.service.country;

import com.bondarenko.movieland.api.model.CountryResponse;
import com.bondarenko.movieland.entity.Country;
import com.bondarenko.movieland.mapper.CountryMapper;
import com.bondarenko.movieland.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    @Override
    public List<CountryResponse> findByIdIn(List<Long> countryIds) {
        List<Country> countries = countryRepository.findByIdIn(countryIds);
        return countryMapper.toCountriesResponse(countries);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @Override
    public List<CountryResponse> findByMovieId(Long id) {
        List<Country> countries = countryRepository.findByMovieId(id);
        return countryMapper.toCountriesResponse(countries);

    }
    @Override
    public List<CountryResponse> findAll() {
        List<Country> countries = countryRepository.findAll();
        List<CountryResponse> response = countryMapper.toCountriesResponse(countries);
        return new ArrayList<>(response);
    }
}
