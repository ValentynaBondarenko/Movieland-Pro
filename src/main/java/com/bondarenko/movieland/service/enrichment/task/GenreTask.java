package com.bondarenko.movieland.service.enrichment.task;

import com.bondarenko.movieland.api.model.GenreResponse;
import com.bondarenko.movieland.api.model.MovieDto;
import com.bondarenko.movieland.service.genre.GenreService;
import com.bondarenko.movieland.util.TimeLoggerUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class GenreTask implements Runnable {
    private final GenreService genreService;
    @Setter
    private MovieDto movieDto;


    @Override
    public void run() {
        long start = TimeLoggerUtil.start("Genre");

        List<Long> genresIds = Optional.of(movieDto.getGenres())
                .orElse(List.of())
                .stream()
                .map(GenreResponse::getId)
                .toList();

        List<GenreResponse> genres = genreService.findByIdIn(genresIds);
        movieDto.setGenres(genres);

        TimeLoggerUtil.end("Genre", start);
        log.info("Genre task finished with genres: {}", genres);
    }


}
