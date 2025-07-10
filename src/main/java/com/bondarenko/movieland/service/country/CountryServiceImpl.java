package com.bondarenko.movieland.service.country;

import com.bondarenko.movieland.entity.Country;
import com.bondarenko.movieland.repository.CountryRepository;
import com.bondarenko.movieland.service.cache.CountryCacheProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public Set<Country> findByIdIn(Set<Long> countryIds) {
        return countryRepository.findByIdIn(countryIds);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @Override
    public Set<Country> findByMovieId(Long id) {
        return countryRepository.findByMovieId(id);
    }
    @Override
    public List<Country> findAll(){
        return countryRepository.findAll();
    }
}
