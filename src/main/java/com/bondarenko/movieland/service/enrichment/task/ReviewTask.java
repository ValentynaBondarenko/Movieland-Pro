package com.bondarenko.movieland.service.enrichment.task;

import com.bondarenko.movieland.api.model.MovieRequest;
import com.bondarenko.movieland.api.model.ReviewResponse;
import com.bondarenko.movieland.entity.Movie;
import com.bondarenko.movieland.entity.Review;
import com.bondarenko.movieland.service.review.ReviewService;
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
public class ReviewTask implements Runnable {
    private final ReviewService reviewService;
    @Setter
    private Movie movie;

    @Override
    public void run() {
        long start = TimeLoggerUtil.start("Review");

        List<Long> reviewIds = Optional.of(movie.getReviews())
                .orElse(List.of())
                .stream()
                .map(Review::getId)
                .toList();

        List<Review> reviews = new ArrayList<>(reviewService.findById(reviewIds));
        movie.setReviews(reviews);

        TimeLoggerUtil.end("Review", start);
        log.info("Review task finished with reviews: {}", reviews);
    }

}
