package com.bondarenko.movieland.service.enrichment.task;

import com.bondarenko.movieland.api.model.FullMovieResponse;
import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.api.model.MovieRequest;
import com.bondarenko.movieland.service.genre.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class GenreTask implements Runnable {
    private final GenreService genreService;
    private MovieRequest movieRequest;
    @Setter
    private FullMovieResponse fullMovieResponse;


    @Override
    public void run() {
        List<Long> genresIds = Optional.of(movieRequest.getGenres())
                .orElse(List.of())
                .stream()
                .map(GenreResponse::getId)
                .toList();

        List<GenreResponse> genres = genreService.findByIdIn(genresIds);
        fullMovieResponse.setGenres(genres);
    }


}
