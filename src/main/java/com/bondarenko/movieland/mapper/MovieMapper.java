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
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name_ukrainian", source = "nameUkrainian")
    @Mapping(target = "name_native", source = "nameNative")
    @Mapping(target = "year_of_release", source = "yearOfRelease")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "rating", source = "rating")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "picturePath", source = "poster")
    List<ResponseMovieDTO> toMovieDTO(List<Movie> movies);

    Movie toMovie(ResponseMovieDTO movies);

}
