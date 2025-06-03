package com.bondarenko.movieland.configuration;

import com.bondarenko.movieland.entity.Country;
import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.repository.CountryRepository;
import com.bondarenko.movieland.repository.GenreRepository;
import com.bondarenko.movieland.service.cache.CustomCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfiguration {
//    @Bean
//    public CustomCache<Genre> genreCache(GenreRepository genreRepository) {
//        return new CustomCache<>(genreRepository::findAll);
//    }
//
//    @Bean
//    public CustomCache<Country> countryCache(CountryRepository countryRepository) {
//        return new CustomCache<>(countryRepository::findAll);
//    }
}
