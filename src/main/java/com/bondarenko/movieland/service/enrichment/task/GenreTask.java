package com.bondarenko.movieland.service.enrichment.task;

import com.bondarenko.movieland.entity.Genre;
import com.bondarenko.movieland.entity.Movie;
import com.bondarenko.movieland.service.genre.GenreService;
import com.bondarenko.movieland.util.TimeLoggerUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class GenreTask implements Runnable {
    private final GenreService genreService;
    @Setter
    private Movie movie;

    @Override
    public void run() {
        long start = TimeLoggerUtil.start("Genre");

        List<Long> genresIds = Optional.of(movie.getGenres())
                .orElse(List.of())
                .stream()
                .map(Genre::getId)
                .toList();

        List<Genre> genres = new ArrayList<>(genreService.findById(genresIds));
        movie.setGenres(genres);

        TimeLoggerUtil.end("Genre", start);
        log.info("Genre task finished with genres: {}", genres);
    }


}
