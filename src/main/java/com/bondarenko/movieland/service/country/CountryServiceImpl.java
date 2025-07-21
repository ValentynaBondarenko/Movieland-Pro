package com.bondarenko.movieland.service.country;

import com.bondarenko.movieland.api.model.CountryResponse;
import com.bondarenko.movieland.entity.Country;
import com.bondarenko.movieland.mapper.CountryMapper;
import com.bondarenko.movieland.repository.CountryRepository;
import com.bondarenko.movieland.service.cache.CountryCacheProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    @Override
    public Set<CountryResponse> findByIdIn(Set<Long> countryIds) {
        Set<Country> countries = countryRepository.findByIdIn(countryIds);
        return countryMapper.toCountriesResponse(countries);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @Override
    public Set<CountryResponse> findByMovieId(Long id) {
        Set<Country> countries = countryRepository.findByMovieId(id);
        return countryMapper.toCountriesResponse(countries);

    }
    @Override
    public List<CountryResponse> findAll() {
        List<Country> countries = countryRepository.findAll();
        Set<CountryResponse> responseSet = countryMapper.toCountriesResponse(new HashSet<>(countries));
        return new ArrayList<>(responseSet);
    }
}
