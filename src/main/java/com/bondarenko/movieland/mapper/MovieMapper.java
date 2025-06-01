package com.bondarenko.movieland.mapper;

import com.bondarenko.movieland.api.model.*;
import com.bondarenko.movieland.entity.*;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MovieMapper {

    @Mapping(target = "name_ukrainian", source = "nameUkrainian")
    @Mapping(target = "name_native", source = "nameNative")
    @Mapping(target = "year_of_release", source = "yearOfRelease")
    @Mapping(target = "picturePath", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    List<ResponseMovie> toMovieResponse(List<Movie> movies);

    @Mapping(target = "reviews", ignore = true)
    @Mapping(source = "poster", target = "picturePath")
    ResponseFullMovie toMovieResponse(Movie movies);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "poster", source = "picturePath")
    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "countries", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    Movie toMovie(MovieRequest movieRequest);

    @Mapping(target = "picturePath", source = "poster")
    @Mapping(target = "genres", qualifiedByName = "mapGenres")
    @Mapping(target = "countries", qualifiedByName = "mapCountries")
    @Mapping(target = "reviews", qualifiedByName = "mapReviews")
    ResponseFullMovie toFullMovie(Movie movie);

    @Named("mapGenres")
    @IterableMapping(qualifiedByName = "mapGenre")
    List<ResponseGenre> mapGenres(Set<Genre> genres);

    @Named("mapGenre")
    ResponseGenre mapGenre(Genre genre);

    @Named("mapCountries")
    @IterableMapping(qualifiedByName = "mapCountry")
    List<ResponseCountry> mapCountries(Set<Country> countries);

    @Named("mapCountry")
    ResponseCountry mapCountry(Country country);

    @Named("mapUsers")
    @IterableMapping(qualifiedByName = "mapUser")
    List<ResponseUser> mapUsers(List<User> users);

    @Named("mapUser")
    ResponseUser mapUser(User user);

    @Named("mapReviews")
    @IterableMapping(qualifiedByName = "mapReview")
    List<ResponseReview> mapReviews(List<Review> reviews);

    @Named("mapReview")
    ResponseReview mapReview(Review review);
}
