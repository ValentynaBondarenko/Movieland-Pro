package com.bondarenko.movieland.mapper;

import com.bondarenko.movieland.api.model.*;
import com.bondarenko.movieland.entity.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MovieMapper {

    @Mapping(target = "name_ukrainian", source = "nameUkrainian")
    @Mapping(target = "name_native", source = "nameNative")
    @Mapping(target = "year_of_release", source = "yearOfRelease")
    @Mapping(target = "picturePath", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    List<MovieResponse> toMovieResponse(List<Movie> movies);

    @Mapping(target = "reviews", ignore = true)
    @Mapping(source = "poster", target = "picturePath")
    FullMovieResponse toMovieResponse(Movie movies);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "poster", source = "picturePath")
    @Mapping(target = "genres", qualifiedByName = "mapGenresFromDto")
    @Mapping(target = "countries", qualifiedByName = "mapCountriesFromDto")
    @Mapping(target = "reviews", ignore = true)
    Movie toMovie(FullMovieResponse fullMovieResponse);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "poster", source = "picturePath")
    @Mapping(target = "genres", qualifiedByName = "mapGenresFromDto")
    @Mapping(target = "countries", qualifiedByName = "mapCountriesFromDto")
    @Mapping(target = "reviews", ignore = true)
    Movie toMovie(MovieDto MovieDto);

    @Mapping(target = "picturePath", source = "poster")
    @Mapping(target = "genres", qualifiedByName = "mapGenres")
    @Mapping(target = "countries", qualifiedByName = "mapCountries")
    @Mapping(target = "reviews", qualifiedByName = "mapReviews")
    FullMovieResponse toFullMovie(Movie movie);

    @Mapping(target = "picturePath", source = "poster")
    @Mapping(target = "genres", qualifiedByName = "mapGenres")
    @Mapping(target = "countries", qualifiedByName = "mapCountries")
//    @Mapping(target = "review", qualifiedByName = "mapReviews")
    MovieDto toMovieDto(Movie movie);

    @Named("mapGenres")
    @IterableMapping(qualifiedByName = "mapGenre")
    List<GenreResponse> mapGenres(List<Genre> genres);

    @Named("mapGenre")
    GenreResponse mapGenre(Genre genre);

    @Named("mapCountries")
    @IterableMapping(qualifiedByName = "mapCountry")
    List<CountryResponse> mapCountries(List<Country> countries);

    @Named("mapCountry")
    CountryResponse mapCountry(Country country);

    @Named("mapUsers")
    @IterableMapping(qualifiedByName = "mapUser")
    List<UserIdResponse> mapUsers(List<User> users);

    @Named("mapUser")
    UserIdResponse mapUser(User user);

    @Named("mapReviews")
    @IterableMapping(qualifiedByName = "mapReview")
    List<ReviewResponse> mapReviews(List<Review> reviews);

    @Named("mapReview")
    ReviewResponse mapReview(Review review);

    @Named("mapGenresFromDto")
    @IterableMapping(qualifiedByName = "mapGenreFromDto")
    List<Genre> mapGenresFromDto(List<GenreResponse> genres);

    @Named("mapGenreFromDto")
    default Genre mapGenreFromDto(GenreResponse genreResponse) {
        if (genreResponse == null) return null;
        Genre genre = new Genre();
        genre.setId(genreResponse.getId());
        genre.setName(genreResponse.getName());
        return genre;
    }

    @Named("mapCountriesFromDto")
    @IterableMapping(qualifiedByName = "mapCountryFromDto")
    List<Country> mapCountriesFromDto(List<CountryResponse> countries);

    @Named("mapCountryFromDto")
    default Country mapCountryFromDto(CountryResponse countryResponse) {
        if (countryResponse == null) return null;
        Country country = new Country();
        country.setId(countryResponse.getId());
        country.setName(countryResponse.getName());
        return country;
    }


}
