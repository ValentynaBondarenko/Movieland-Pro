package com.bondarenko.movieland.service.review;

import com.bondarenko.movieland.api.model.ReviewResponse;

import java.util.List;

public interface ReviewService {
    List<ReviewResponse> findByMovieId(Long id);
}
