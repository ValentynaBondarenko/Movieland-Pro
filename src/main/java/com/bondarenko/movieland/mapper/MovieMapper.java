package com.bondarenko.movieland.mapper;

import com.bondarenko.movieland.api.model.ResponseFullMovie;
import com.bondarenko.movieland.api.model.ResponseGenre;
import com.bondarenko.movieland.api.model.ResponseMovie;
import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.entity.Movie;
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
    ResponseFullMovie toFullMovie(Movie movie);

    @Named("mapGenres")
    @IterableMapping(qualifiedByName = "mapGenre")
    List<ResponseGenre> mapGenres(List<Genre> genres);

    @Named("mapGenre")
    ResponseGenre mapGenre(Genre genre);
}
