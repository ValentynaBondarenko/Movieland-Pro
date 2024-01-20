package com.bondarenko.movieland.mapper;

import com.bondarenko.movieland.api.model.*;
import com.bondarenko.movieland.entity.*;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface MovieMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name_ukrainian", source = "nameUkrainian")
    @Mapping(target = "name_native", source = "nameNative")
    @Mapping(target = "year_of_release", source = "yearOfRelease")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "rating", source = "rating")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "picturePath", source = "poster")
    List<ResponseMovie> toMovieDTO(List<Movie> movies);

    Movie toMovie(ResponseMovie movies);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "nameUkrainian", source = "nameUkrainian")
    @Mapping(target = "nameNative", source = "nameNative")
    @Mapping(target = "yearOfRelease", source = "yearOfRelease")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "rating", source = "rating")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "picturePath", source = "poster")
    @Mapping(target = "genres", qualifiedByName = "mapGenres")
    @Mapping(target = "countries", qualifiedByName = "mapCountries")
    @Mapping(target = "reviews", qualifiedByName = "mapReviews")
    //@Mapping(target = "users", qualifiedByName = "mapUsers")
    ResponseFullMovie toFullMovie(Movie movie);

    @Named("mapGenres")
    @IterableMapping(qualifiedByName = "mapGenre")
    List<ResponseGenre> mapGenres(List<Genre> genres);

    @Named("mapGenre")
    ResponseGenre mapGenre(Genre genre);

    @Named("mapCountries")
    @IterableMapping(qualifiedByName = "mapCountry")
    List<ResponseCountry> mapCountries(List<Country> countries);

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
