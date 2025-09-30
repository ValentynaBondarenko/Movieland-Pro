package com.bondarenko.movieland.service.review;

import com.bondarenko.movieland.api.model.ReviewResponse;
import com.bondarenko.movieland.entity.Review;
import com.bondarenko.movieland.mapper.ReviewMapper;
import com.bondarenko.movieland.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @Override
    public List<ReviewResponse> findByMovieId(Long id) {
        List<Review> reviews = reviewRepository.findByMovieId(id);
        return reviewMapper.toReviewResponse(reviews);
    }

    @Override
    public List<ReviewResponse> findByIdIn(List<Long> reviewIds) {
        List<Review> reviews = reviewRepository.findAll()
                .stream()
                .filter(review -> reviewIds.contains(review.getId()))
                .toList();
        return reviewMapper.toReviewResponse(reviews);
    }

    @Override
    public List<Review> findById(List<Long> reviewIds) {
        return reviewRepository.findAll()
                .stream()
                .filter(review -> reviewIds.contains(review.getId()))
                .toList();
    }
}
