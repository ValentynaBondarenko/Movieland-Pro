package com.bondarenko.movieland.service.review;

import com.bondarenko.movieland.api.model.ReviewResponse;
import com.bondarenko.movieland.entity.Review;

import java.util.List;

public interface ReviewService {
    List<ReviewResponse> findByMovieId(Long id);

    List<ReviewResponse> findByIdIn(List<Long> reviewIds);

    List<Review> findById(List<Long> reviewIds);

}
