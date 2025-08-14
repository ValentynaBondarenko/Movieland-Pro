package com.bondarenko.movieland.service.country;

import com.bondarenko.movieland.api.model.CountryResponse;
import com.bondarenko.movieland.entity.Country;
import com.bondarenko.movieland.exception.CountryNotFoundException;
import com.bondarenko.movieland.mapper.CountryMapper;
import com.bondarenko.movieland.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    @Override
    public List<CountryResponse> findByIdIn(List<Long> countryIds) {
        return countryRepository.findByIdIn(countryIds)
                .map(countryMapper::toCountriesResponse)
                .orElseThrow(() -> new CountryNotFoundException("No countries found for ids: " + countryIds));



    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @Override
    public List<CountryResponse> findByMovieId(Long id) {
       return countryRepository.findByMovieId(id)
               .map(countryMapper::toCountriesResponse)
               .orElseThrow(() -> new CountryNotFoundException("No countries found for movie id: " + id));
    }

    @Override
    public List<CountryResponse> findAll() {
        List<Country> countries = countryRepository.findAll();
        return countryMapper.toCountriesResponse(countries);
    }
}
