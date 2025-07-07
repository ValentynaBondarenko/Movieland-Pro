package com.bondarenko.movieland.service.review;

import com.bondarenko.movieland.entity.Review;

import java.util.Set;

public interface ReviewService {
    Set<Review> findByMovieId(Long id);
}
