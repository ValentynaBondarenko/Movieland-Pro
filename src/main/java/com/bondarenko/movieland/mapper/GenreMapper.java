package com.bondarenko.movieland.mapper;

import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GenreMapper {
    Set<GenreResponse> toGenreResponse(Set<Genre> genres);
}
