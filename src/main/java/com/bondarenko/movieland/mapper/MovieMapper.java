package com.bondarenko.movieland.mapper;

import com.bondarenko.movieland.api.model.ResponseMovieDTO;
import com.bondarenko.movieland.entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface MovieMapper {
    @Mapping(target = "id", source = "movie_id")
    @Mapping(target = "name_ua", source = "nameUkrainian")
    @Mapping(target = "name_native", source = "nameNative")
    @Mapping(target = "release_year", source = "yearOfRelease")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "rating", source = "rating")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "picturePath", source = "poster")
    List<ResponseMovieDTO> toMovieDTO(List<Movie> movies);
}
