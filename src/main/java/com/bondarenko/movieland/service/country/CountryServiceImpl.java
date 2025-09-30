package com.bondarenko.movieland.service.country;

import com.bondarenko.movieland.api.model.CountryResponse;
import com.bondarenko.movieland.entity.Country;
import com.bondarenko.movieland.exception.CountryNotFoundException;
import com.bondarenko.movieland.mapper.CountryMapper;
import com.bondarenko.movieland.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;
    @Override
    public List<CountryResponse> findByIdIn(List<Long> countryIds) {
        List<Country> countries = countryRepository.findByIdIn(countryIds);
        if (countries.isEmpty()) {
            throw new CountryNotFoundException("No countries found for ids: " + countryIds);
        }
        return countryMapper.toCountriesResponse(countries);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Country> findById(List<Long> countryIds) {
        List<Country> countries = countryRepository.findByIdIn(countryIds);
        if (countries.isEmpty()) {
            throw new CountryNotFoundException("No countries found for ids: " + countryIds);
        }
        return countries;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @Override
    public List<CountryResponse> findByMovieId(Long id) {
        List<Country> countries = countryRepository.findByMovieId(id);
        if (countries.isEmpty()) {
            throw new CountryNotFoundException("No countries found for movie id: " + id);
        }
        return countryMapper.toCountriesResponse(countries);
    }

    @Override
    public List<CountryResponse> findAll() {
        List<Country> countries = countryRepository.findAll();
        return countryMapper.toCountriesResponse(countries);
    }
}
