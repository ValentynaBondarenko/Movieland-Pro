package com.bondarenko.movieland.mapper;

import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GenreMapper {
    List<GenreResponse> toGenreResponse(List<Genre> genres);

    GenreResponse toGenreResponse(Genre genre);

    List<Genre> toGenre(List<GenreResponse> genreResponses);
}
