package com.bondarenko.movieland.service.enrichment.task;

import com.bondarenko.movieland.api.model.FullMovieResponse;
import com.bondarenko.movieland.api.model.MovieRequest;
import com.bondarenko.movieland.api.model.ReviewResponse;
import com.bondarenko.movieland.service.review.ReviewService;
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
public class ReviewTask implements Runnable {
    private final ReviewService reviewService;
    @Setter
    private FullMovieResponse fullMovieResponse;
    private MovieRequest movieRequest;

    @Override
    public void run() {
        List<Long> reviewIds = Optional.of(movieRequest.getReview())
                .orElse(List.of())
                .stream()
                .map(ReviewResponse::getId)
                .toList();

        List<ReviewResponse> reviews = reviewService.findByIdIn(reviewIds);
        fullMovieResponse.setReviews(reviews);
    }

}
