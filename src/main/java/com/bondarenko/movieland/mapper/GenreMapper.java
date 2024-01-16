package com.bondarenko.movieland.mapper;

import com.bondarenko.movieland.api.model.ResponseGenre;
import com.bondarenko.movieland.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface GenreMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    List<ResponseGenre> toGenreDTO(List<Genre> genres);
}
